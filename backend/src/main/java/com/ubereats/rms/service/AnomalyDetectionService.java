package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.dto.AnomalyDetectionResult;
import com.ubereats.rms.exception.AnalysisException;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnomalyDetectionService {

    @Autowired
    private OrderRepository orderRepository;

    // Thresholds for anomaly detection (configurable)
    private static final double REVENUE_ANOMALY_THRESHOLD = 2.0; // 2 standard deviations
    private static final double ORDER_COUNT_ANOMALY_THRESHOLD = 2.5; // 2.5 standard deviations
    private static final double CANCELLATION_ANOMALY_THRESHOLD = 0.15; // 15% cancellation rate

    public AnomalyDetectionResult detectAnomalies(LocalDateTime startDate, LocalDateTime endDate, String outletId) {
        try {
            List<Order> orders = orderRepository.findAll();

            List<Order> filteredOrders = orders.stream()
                    .filter(order -> isWithinDateRange(order, startDate, endDate))
                    .filter(order -> outletId == null || outletId.isEmpty() ||
                            outletId.equals(order.getOutletId()))
                    .collect(Collectors.toList());

            AnomalyDetectionResult result = new AnomalyDetectionResult();

            // Detect revenue anomalies
            Map<String, Double> revenueAnomalies = detectRevenueAnomalies(filteredOrders);
            result.setRevenueAnomalies(revenueAnomalies);

            // Detect order count anomalies
            Map<String, Long> orderCountAnomalies = detectOrderCountAnomalies(filteredOrders);
            result.setOrderCountAnomalies(orderCountAnomalies);

            // Detect cancellation anomalies
            Map<String, Double> cancellationAnomalies = detectCancellationAnomalies(filteredOrders);
            result.setCancellationAnomalies(cancellationAnomalies);

            // Detect unusual payment patterns
            Map<String, Map<String, Double>> paymentAnomalies = detectPaymentAnomalies(filteredOrders);
            result.setPaymentAnomalies(paymentAnomalies);

            // Detect time-based anomalies
            Map<String, Anomaly> timeBasedAnomalies = detectTimeBasedAnomalies(filteredOrders);
            result.setTimeBasedAnomalies(timeBasedAnomalies);

            // Detect outlet-specific anomalies
            Map<String, List<Anomaly>> outletAnomalies = detectOutletAnomalies(orders, startDate, endDate);
            result.setOutletAnomalies(outletAnomalies);

            // Calculate overall anomaly score
            result.setOverallAnomalyScore(calculateAnomalyScore(result));

            // Generate recommendations
            result.setRecommendations(generateRecommendations(result));

            return result;

        } catch (Exception e) {
            throw new AnalysisException("ANOMALY_DETECTION", "AnomalyDetectionService",
                    "Failed to detect anomalies", e);
        }
    }

    private boolean isWithinDateRange(Order order, LocalDateTime start, LocalDateTime end) {
        return order.getOrderPlaced() != null &&
                !order.getOrderPlaced().isBefore(start) &&
                !order.getOrderPlaced().isAfter(end);
    }

    private Map<String, Double> detectRevenueAnomalies(List<Order> orders) {
        Map<String, Double> anomalies = new HashMap<>();

        // Group revenue by day
        Map<String, Double> dailyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().toLocalDate().toString(),
                        Collectors.summingDouble(Order::getTotalPriceLkr)
                ));

        if (dailyRevenue.size() < 2) return anomalies;

        // Calculate statistics
        List<Double> revenues = new ArrayList<>(dailyRevenue.values());
        double mean = revenues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double stdDev = calculateStandardDeviation(revenues, mean);

        // Identify anomalies (beyond threshold standard deviations)
        for (Map.Entry<String, Double> entry : dailyRevenue.entrySet()) {
            double zScore = Math.abs((entry.getValue() - mean) / stdDev);
            if (zScore > REVENUE_ANOMALY_THRESHOLD) {
                anomalies.put(entry.getKey(), zScore);
            }
        }

        return anomalies;
    }

    private Map<String, Long> detectOrderCountAnomalies(List<Order> orders) {
        Map<String, Long> anomalies = new HashMap<>();

        // Group orders by hour
        Map<String, Long> hourlyOrders = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> String.format("%s %02d:00",
                                order.getOrderPlaced().toLocalDate().toString(),
                                order.getOrderPlaced().getHour()),
                        Collectors.counting()
                ));

        if (hourlyOrders.size() < 2) return anomalies;

        // Calculate statistics
        List<Long> counts = new ArrayList<>(hourlyOrders.values());
        double mean = counts.stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = calculateStandardDeviationForLongs(counts, mean);

        // Identify anomalies
        for (Map.Entry<String, Long> entry : hourlyOrders.entrySet()) {
            double zScore = Math.abs((entry.getValue() - mean) / stdDev);
            if (zScore > ORDER_COUNT_ANOMALY_THRESHOLD) {
                anomalies.put(entry.getKey(), entry.getValue());
            }
        }

        return anomalies;
    }

    private Map<String, Double> detectCancellationAnomalies(List<Order> orders) {
        Map<String, Double> anomalies = new HashMap<>();

        // Group by outlet and calculate cancellation rate
        Map<String, List<Order>> ordersByOutlet = orders.stream()
                .collect(Collectors.groupingBy(Order::getOutletId));

        for (Map.Entry<String, List<Order>> entry : ordersByOutlet.entrySet()) {
            long totalOrders = entry.getValue().size();
            long cancelledOrders = entry.getValue().stream()
                    .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                    .count();

            double cancellationRate = (double) cancelledOrders / totalOrders;
            if (cancellationRate > CANCELLATION_ANOMALY_THRESHOLD) {
                anomalies.put(entry.getKey(), cancellationRate * 100); // Convert to percentage
            }
        }

        return anomalies;
    }

    private Map<String, Map<String, Double>> detectPaymentAnomalies(List<Order> orders) {
        Map<String, Map<String, Double>> anomalies = new HashMap<>();

        // Analyze payment method distribution by hour
        Map<String, Map<String, Long>> paymentByHour = new HashMap<>();

        for (Order order : orders) {
            String hourKey = String.format("%02d:00", order.getOrderPlaced().getHour());
            String paymentMethod = order.getPaymentMethod();

            Map<String, Long> hourData = paymentByHour.getOrDefault(hourKey, new HashMap<>());
            hourData.put(paymentMethod, hourData.getOrDefault(paymentMethod, 0L) + 1);
            paymentByHour.put(hourKey, hourData);
        }

        // Identify unusual patterns
        for (Map.Entry<String, Map<String, Long>> hourEntry : paymentByHour.entrySet()) {
            Map<String, Double> paymentPercentages = new HashMap<>();
            long total = hourEntry.getValue().values().stream().mapToLong(Long::longValue).sum();

            for (Map.Entry<String, Long> paymentEntry : hourEntry.getValue().entrySet()) {
                double percentage = (double) paymentEntry.getValue() / total * 100;
                if (percentage > 80) { // If one payment method dominates >80%
                    paymentPercentages.put(paymentEntry.getKey(), percentage);
                }
            }

            if (!paymentPercentages.isEmpty()) {
                anomalies.put(hourEntry.getKey(), paymentPercentages);
            }
        }

        return anomalies;
    }

    private Map<String, Anomaly> detectTimeBasedAnomalies(List<Order> orders) {
        Map<String, Anomaly> anomalies = new HashMap<>();

        // Check for unusually long preparation times
        for (Order order : orders) {
            if (order.getPrepStarted() != null && order.getPrepFinished() != null) {
                long prepMinutes = java.time.Duration.between(
                        order.getPrepStarted(), order.getPrepFinished()).toMinutes();

                if (prepMinutes > 60) { // More than 60 minutes preparation
                    Anomaly anomaly = new Anomaly();
                    anomaly.setType("LONG_PREPARATION_TIME");
                    anomaly.setValue(prepMinutes);
                    anomaly.setThreshold(60);
                    anomaly.setOrderId(order.getOrderId());
                    anomalies.put(order.getOrderId(), anomaly);
                }
            }

            // Check for unusual order values
            if (order.getTotalPriceLkr() > 10000) { // Orders over 10,000 LKR
                Anomaly anomaly = new Anomaly();
                anomaly.setType("HIGH_VALUE_ORDER");
                anomaly.setValue(order.getTotalPriceLkr());
                anomaly.setThreshold(10000);
                anomaly.setOrderId(order.getOrderId());
                anomalies.put(order.getOrderId(), anomaly);
            }
        }

        return anomalies;
    }

    private Map<String, List<Anomaly>> detectOutletAnomalies(List<Order> allOrders, LocalDateTime start, LocalDateTime end) {
        Map<String, List<Anomaly>> outletAnomalies = new HashMap<>();

        // Group orders by outlet
        Map<String, List<Order>> ordersByOutlet = allOrders.stream()
                .filter(order -> isWithinDateRange(order, start, end))
                .collect(Collectors.groupingBy(Order::getOutletId));

        // Calculate baseline metrics from all outlets
        Map<String, Double> outletMetrics = new HashMap<>();
        for (Map.Entry<String, List<Order>> entry : ordersByOutlet.entrySet()) {
            double avgOrderValue = entry.getValue().stream()
                    .mapToDouble(Order::getTotalPriceLkr)
                    .average()
                    .orElse(0);
            outletMetrics.put(entry.getKey(), avgOrderValue);
        }

        // Calculate overall average
        double overallAvg = outletMetrics.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        // Identify outlier outlets
        for (Map.Entry<String, Double> entry : outletMetrics.entrySet()) {
            double deviation = Math.abs((entry.getValue() - overallAvg) / overallAvg);
            if (deviation > 0.3) { // More than 30% deviation from average
                Anomaly anomaly = new Anomaly();
                anomaly.setType("OUTLET_PERFORMANCE_ANOMALY");
                anomaly.setValue(entry.getValue());
                anomaly.setThreshold(overallAvg);
                anomaly.setOutletId(entry.getKey());

                List<Anomaly> anomalies = outletAnomalies.getOrDefault(entry.getKey(), new ArrayList<>());
                anomalies.add(anomaly);
                outletAnomalies.put(entry.getKey(), anomalies);
            }
        }

        return outletAnomalies;
    }

    private double calculateAnomalyScore(AnomalyDetectionResult result) {
        int totalAnomalies = 0;
        int detectedAnomalies = 0;

        totalAnomalies += result.getRevenueAnomalies().size();
        totalAnomalies += result.getOrderCountAnomalies().size();
        totalAnomalies += result.getCancellationAnomalies().size();
        totalAnomalies += result.getPaymentAnomalies().size();
        totalAnomalies += result.getTimeBasedAnomalies().size();
        totalAnomalies += result.getOutletAnomalies().values().stream()
                .mapToInt(List::size)
                .sum();

        // Normalize score (0-100)
        double score = Math.min(100, (double) detectedAnomalies / Math.max(1, totalAnomalies) * 100);
        return score;
    }

    private List<String> generateRecommendations(AnomalyDetectionResult result) {
        List<String> recommendations = new ArrayList<>();

        if (!result.getRevenueAnomalies().isEmpty()) {
            recommendations.add("Investigate revenue anomalies on high-variance days");
        }

        if (!result.getCancellationAnomalies().isEmpty()) {
            recommendations.add("Review cancellation policies for outlets with high cancellation rates");
        }

        if (!result.getTimeBasedAnomalies().isEmpty()) {
            recommendations.add("Monitor orders with unusually long preparation times");
        }

        if (result.getOverallAnomalyScore() > 50) {
            recommendations.add("Consider implementing real-time anomaly monitoring system");
        }

        return recommendations;
    }

    private double calculateStandardDeviation(List<Double> values, double mean) {
        double variance = values.stream()
                .mapToDouble(val -> Math.pow(val - mean, 2))
                .average()
                .orElse(0);
        return Math.sqrt(variance);
    }

    private double calculateStandardDeviationForLongs(List<Long> values, double mean) {
        double variance = values.stream()
                .mapToDouble(val -> Math.pow(val - mean, 2))
                .average()
                .orElse(0);
        return Math.sqrt(variance);
    }

    // Inner classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnomalyDetectionResult {
        private Map<String, Double> revenueAnomalies;
        private Map<String, Long> orderCountAnomalies;
        private Map<String, Double> cancellationAnomalies;
        private Map<String, Map<String, Double>> paymentAnomalies;
        private Map<String, Anomaly> timeBasedAnomalies;
        private Map<String, List<Anomaly>> outletAnomalies;
        private double overallAnomalyScore;
        private List<String> recommendations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Anomaly {
        private String type;
        private double value;
        private double threshold;
        private String orderId;
        private String outletId;
        private LocalDateTime detectedAt;

        public Anomaly() {
            this.detectedAt = LocalDateTime.now();
        }

        public String getDescription() {
            return String.format("%s detected: Value=%.2f, Threshold=%.2f",
                    type, value, threshold);
        }
    }
}