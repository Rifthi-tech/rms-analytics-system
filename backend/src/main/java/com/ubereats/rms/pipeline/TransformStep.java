package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransformStep implements PipelineStep {

    private List<Transformation> transformations;

    public TransformStep() {
        // Default transformations
        this.transformations = List.of(
                new CurrencyConversion(),
                new TimezoneConversion(),
                new DataNormalization(),
                new FeatureEngineering()
        );
    }

    public TransformStep(List<Transformation> customTransformations) {
        this.transformations = customTransformations;
    }

    @Override
    public String getName() {
        return "TransformStep";
    }

    @Override
    public List<Order> process(List<Order> input) {
        List<Order> transformed = input;

        for (Transformation transformation : transformations) {
            transformed = transformation.apply(transformed);
        }

        return transformed;
    }

    public TransformStep addTransformation(Transformation transformation) {
        this.transformations.add(transformation);
        return this;
    }

    // Transformation interface
    public interface Transformation extends Function<List<Order>, List<Order>> {
        String getName();
    }

    // Currency Conversion Transformation
    private static class CurrencyConversion implements Transformation {
        private static final double LKR_TO_USD_RATE = 0.0031; // Example rate

        @Override
        public String getName() {
            return "CurrencyConversion";
        }

        @Override
        public List<Order> apply(List<Order> orders) {
            return orders.stream()
                    .map(order -> {
                        order.setTotalPriceUsd(order.getTotalPriceLkr() * LKR_TO_USD_RATE);

                        // Convert item prices
                        order.getItems().forEach(item -> {
                            item.setPriceUsd(item.getPriceLkr() * LKR_TO_USD_RATE);
                        });

                        return order;
                    })
                    .collect(Collectors.toList());
        }
    }

    // Timezone Conversion Transformation
    private static class TimezoneConversion implements Transformation {
        private static final int TIMEZONE_OFFSET = 5; // Sri Lanka is UTC+5:30

        @Override
        public String getName() {
            return "TimezoneConversion";
        }

        @Override
        public List<Order> apply(List<Order> orders) {
            return orders.stream()
                    .map(order -> {
                        // Convert all timestamps to UTC
                        order.setOrderPlacedUtc(order.getOrderPlaced().minusHours(TIMEZONE_OFFSET));
                        order.setOrderConfirmedUtc(order.getOrderConfirmed() != null ?
                                order.getOrderConfirmed().minusHours(TIMEZONE_OFFSET) : null);
                        order.setPrepStartedUtc(order.getPrepStarted() != null ?
                                order.getPrepStarted().minusHours(TIMEZONE_OFFSET) : null);
                        order.setPrepFinishedUtc(order.getPrepFinished() != null ?
                                order.getPrepFinished().minusHours(TIMEZONE_OFFSET) : null);
                        order.setServedTimeUtc(order.getServedTime() != null ?
                                order.getServedTime().minusHours(TIMEZONE_OFFSET) : null);

                        return order;
                    })
                    .collect(Collectors.toList());
        }
    }

    // Data Normalization Transformation
    private static class DataNormalization implements Transformation {

        @Override
        public String getName() {
            return "DataNormalization";
        }

        @Override
        public List<Order> apply(List<Order> orders) {
            return orders.stream()
                    .map(order -> {
                        // Normalize text fields
                        if (order.getPaymentMethod() != null) {
                            order.setPaymentMethod(normalizePaymentMethod(order.getPaymentMethod()));
                        }

                        // Normalize status
                        order.setStatus(normalizeStatus(order.getStatus()));

                        // Clean customer data
                        if (order.getCustomerName() != null) {
                            order.setCustomerName(normalizeName(order.getCustomerName()));
                        }

                        // Ensure numeric fields are positive
                        order.setTotalPriceLkr(Math.abs(order.getTotalPriceLkr()));
                        order.setNumItems(Math.abs(order.getNumItems()));

                        return order;
                    })
                    .collect(Collectors.toList());
        }

        private String normalizePaymentMethod(String method) {
            if (method == null) return "UNKNOWN";

            method = method.trim().toUpperCase();
            if (method.contains("CARD") || method.contains("CREDIT") || method.contains("DEBIT")) {
                return "CARD";
            } else if (method.contains("CASH")) {
                return "CASH";
            } else if (method.contains("ONLINE") || method.contains("DIGITAL")) {
                return "ONLINE";
            } else if (method.contains("WALLET")) {
                return "WALLET";
            }
            return method;
        }

        private OrderStatus normalizeStatus(OrderStatus status) {
            if (status == null) return OrderStatus.PENDING;
            return status;
        }

        private String normalizeName(String name) {
            if (name == null) return "";
            return name.trim();
        }
    }

    // Feature Engineering Transformation
    private static class FeatureEngineering implements Transformation {

        @Override
        public String getName() {
            return "FeatureEngineering";
        }

        @Override
        public List<Order> apply(List<Order> orders) {
            // Calculate overall statistics for feature engineering
            Map<String, Double> outletAvgOrderValue = calculateOutletAverages(orders);
            Map<String, Double> customerAvgOrderValue = calculateCustomerAverages(orders);

            return orders.stream()
                    .map(order -> {
                        // Add derived features
                        order.setIsHighValueOrder(isHighValueOrder(order, outletAvgOrderValue));
                        order.setIsFrequentCustomer(isFrequentCustomer(order, customerAvgOrderValue));
                        order.setOrderComplexity(calculateOrderComplexity(order));
                        order.setTimeOfDayCategory(categorizeTimeOfDay(order));

                        // Calculate basket analysis features
                        order.setHasBeverage(order.getItems().stream()
                                .anyMatch(item -> item.getCategory() == Category.BEVERAGE));
                        order.setHasDessert(order.getItems().stream()
                                .anyMatch(item -> item.getCategory() == Category.DESSERT));
                        order.setIsVegetarianOrder(order.getItems().stream()
                                .allMatch(item -> item.isVegetarian()));

                        // Add seasonality features
                        order.setSeason(getSeason(order.getOrderPlaced().getMonthValue()));
                        order.setIsHoliday(isHoliday(order.getOrderPlaced().toLocalDate()));

                        return order;
                    })
                    .collect(Collectors.toList());
        }

        private Map<String, Double> calculateOutletAverages(List<Order> orders) {
            return orders.stream()
                    .collect(Collectors.groupingBy(
                            Order::getOutletId,
                            Collectors.averagingDouble(Order::getTotalPriceLkr)
                    ));
        }

        private Map<String, Double> calculateCustomerAverages(List<Order> orders) {
            return orders.stream()
                    .collect(Collectors.groupingBy(
                            Order::getCustomerId,
                            Collectors.averagingDouble(Order::getTotalPriceLkr)
                    ));
        }

        private boolean isHighValueOrder(Order order, Map<String, Double> outletAvg) {
            Double outletAverage = outletAvg.get(order.getOutletId());
            if (outletAverage == null) return false;
            return order.getTotalPriceLkr() > outletAverage * 1.5;
        }

        private boolean isFrequentCustomer(Order order, Map<String, Double> customerAvg) {
            Double customerAverage = customerAvg.get(order.getCustomerId());
            return customerAverage != null && customerAverage > 1000; // Example threshold
        }

        private int calculateOrderComplexity(Order order) {
            int complexity = 0;

            // More items = more complex
            if (order.getNumItems() > 5) complexity += 2;
            else if (order.getNumItems() > 3) complexity += 1;

            // Multiple categories = more complex
            long categoryCount = order.getItems().stream()
                    .map(item -> item.getCategory())
                    .distinct()
                    .count();
            if (categoryCount > 3) complexity += 2;
            else if (categoryCount > 2) complexity += 1;

            // Special requirements
            boolean hasSpecialItems = order.getItems().stream()
                    .anyMatch(item -> item.isSpicy() || item.isVegetarian());
            if (hasSpecialItems) complexity += 1;

            return complexity;
        }

        private String categorizeTimeOfDay(Order order) {
            int hour = order.getOrderPlaced().getHour();
            if (hour >= 6 && hour < 12) return "MORNING";
            else if (hour >= 12 && hour < 15) return "LUNCH";
            else if (hour >= 15 && hour < 18) return "AFTERNOON";
            else if (hour >= 18 && hour < 22) return "DINNER";
            else return "LATE_NIGHT";
        }

        private String getSeason(int month) {
            if (month >= 3 && month <= 5) return "SPRING";
            else if (month >= 6 && month <= 8) return "SUMMER";
            else if (month >= 9 && month <= 11) return "AUTUMN";
            else return "WINTER";
        }

        private boolean isHoliday(java.time.LocalDate date) {
            // Simplified holiday check - in real implementation, use a holiday calendar
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            // Example holidays (customize for your region)
            return (month == 1 && day == 1) ||   // New Year
                    (month == 4 && day >= 13 && day <= 14) || // Sinhala/Tamil New Year
                    (month == 12 && day == 25);   // Christmas
        }
    }
}