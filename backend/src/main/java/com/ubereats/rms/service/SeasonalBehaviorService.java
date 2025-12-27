package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.dto.SeasonalBehaviorResult;
import com.ubereats.rms.exception.AnalysisException;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeasonalBehaviorService {

    @Autowired
    private OrderRepository orderRepository;

    // Define festival periods (customize based on your region)
    private static final Map<String, List<Month>> FESTIVAL_PERIODS = Map.of(
            "New Year", Arrays.asList(Month.DECEMBER, Month.JANUARY),
            "Sinhala & Tamil New Year", Arrays.asList(Month.APRIL),
            "Christmas", Arrays.asList(Month.DECEMBER),
            "Ramadan", Arrays.asList(Month.APRIL, Month.MAY), // Approximate
            "Vesak", Arrays.asList(Month.MAY),
            "Deepavali", Arrays.asList(Month.OCTOBER, Month.NOVEMBER)
    );

    public SeasonalBehaviorResult analyzeSeasonalPatterns(LocalDateTime startDate, LocalDateTime endDate, String outletId) {
        try {
            List<Order> orders = orderRepository.findAll();

            List<Order> filteredOrders = orders.stream()
                    .filter(order -> isWithinDateRange(order, startDate, endDate))
                    .filter(order -> outletId == null || outletId.isEmpty() ||
                            outletId.equals(order.getOutletId()))
                    .collect(Collectors.toList());

            SeasonalBehaviorResult result = new SeasonalBehaviorResult();

            // Analyze by month
            Map<Month, Double> revenueByMonth = filteredOrders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderPlaced().getMonth(),
                            Collectors.summingDouble(Order::getTotalPriceLkr)
                    ));

            Map<Month, Long> ordersByMonth = filteredOrders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderPlaced().getMonth(),
                            Collectors.counting()
                    ));

            result.setRevenueByMonth(convertMonthMap(revenueByMonth));
            result.setOrdersByMonth(convertMonthMap(ordersByMonth));

            // Analyze by day of week
            Map<String, Double> revenueByDayOfWeek = filteredOrders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderPlaced().getDayOfWeek().toString(),
                            Collectors.summingDouble(Order::getTotalPriceLkr)
                    ));

            result.setRevenueByDayOfWeek(revenueByDayOfWeek);

            // Compare festival vs regular periods
            Map<String, FestivalAnalysis> festivalAnalysis = analyzeFestivalPeriods(filteredOrders);
            result.setFestivalAnalysis(festivalAnalysis);

            // Identify seasonal trends
            result.setSeasonalTrends(identifySeasonalTrends(filteredOrders));

            // Calculate growth rates
            result.setMonthOverMonthGrowth(calculateMonthOverMonthGrowth(revenueByMonth));
            result.setYearOverYearGrowth(calculateYearOverYearGrowth(orders, startDate, endDate));

            // Predict next season performance
            result.setNextSeasonForecast(predictNextSeason(revenueByMonth));

            return result;

        } catch (Exception e) {
            throw new AnalysisException("SEASONAL_ANALYSIS", "SeasonalBehaviorService",
                    "Failed to analyze seasonal behavior", e);
        }
    }

    private boolean isWithinDateRange(Order order, LocalDateTime start, LocalDateTime end) {
        return order.getOrderPlaced() != null &&
                !order.getOrderPlaced().isBefore(start) &&
                !order.getOrderPlaced().isAfter(end);
    }

    private <T> Map<String, T> convertMonthMap(Map<Month, T> monthMap) {
        Map<String, T> result = new LinkedHashMap<>();
        for (Month month : Month.values()) {
            T value = monthMap.get(month);
            if (value != null) {
                result.put(month.toString(), value);
            }
        }
        return result;
    }

    private Map<String, FestivalAnalysis> analyzeFestivalPeriods(List<Order> orders) {
        Map<String, FestivalAnalysis> analysis = new HashMap<>();

        for (Map.Entry<String, List<Month>> festival : FESTIVAL_PERIODS.entrySet()) {
            String festivalName = festival.getKey();
            List<Month> festivalMonths = festival.getValue();

            // Filter orders during festival months
            List<Order> festivalOrders = orders.stream()
                    .filter(order -> festivalMonths.contains(order.getOrderPlaced().getMonth()))
                    .collect(Collectors.toList());

            // Filter orders from non-festival months in the same year
            List<Order> regularOrders = orders.stream()
                    .filter(order -> !festivalMonths.contains(order.getOrderPlaced().getMonth()))
                    .collect(Collectors.toList());

            FestivalAnalysis fa = new FestivalAnalysis();
            fa.setFestivalRevenue(festivalOrders.stream()
                    .mapToDouble(Order::getTotalPriceLkr)
                    .sum());
            fa.setFestivalOrders(festivalOrders.size());
            fa.setRegularRevenue(regularOrders.stream()
                    .mapToDouble(Order::getTotalPriceLkr)
                    .sum());
            fa.setRegularOrders(regularOrders.size());

            if (fa.getRegularOrders() > 0 && fa.getFestivalOrders() > 0) {
                fa.setAverageFestivalOrderValue(fa.getFestivalRevenue() / fa.getFestivalOrders());
                fa.setAverageRegularOrderValue(fa.getRegularRevenue() / fa.getRegularOrders());
                fa.setRevenueIncreasePercentage(
                        ((fa.getAverageFestivalOrderValue() - fa.getAverageRegularOrderValue()) /
                                fa.getAverageRegularOrderValue()) * 100);
            }

            analysis.put(festivalName, fa);
        }

        return analysis;
    }

    private Map<String, Double> identifySeasonalTrends(List<Order> orders) {
        Map<String, Double> trends = new HashMap<>();

        // Group by month and calculate average order value
        Map<Month, Double> avgByMonth = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getMonth(),
                        Collectors.averagingDouble(Order::getTotalPriceLkr)
                ));

        // Identify upward/downward trends
        double overallAvg = avgByMonth.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        for (Map.Entry<Month, Double> entry : avgByMonth.entrySet()) {
            double deviation = ((entry.getValue() - overallAvg) / overallAvg) * 100;
            if (Math.abs(deviation) > 10) { // More than 10% deviation
                trends.put(entry.getKey().toString(), deviation);
            }
        }

        return trends;
    }

    private double calculateMonthOverMonthGrowth(Map<Month, Double> revenueByMonth) {
        List<Month> months = new ArrayList<>(revenueByMonth.keySet());
        if (months.size() < 2) return 0;

        Collections.sort(months);
        Month current = months.get(months.size() - 1);
        Month previous = months.get(months.size() - 2);

        double currentRevenue = revenueByMonth.get(current);
        double previousRevenue = revenueByMonth.get(previous);

        if (previousRevenue == 0) return 0;

        return ((currentRevenue - previousRevenue) / previousRevenue) * 100;
    }

    private double calculateYearOverYearGrowth(List<Order> allOrders, LocalDateTime start, LocalDateTime end) {
        // Get current year data
        List<Order> currentYearOrders = allOrders.stream()
                .filter(order -> isWithinDateRange(order, start, end))
                .collect(Collectors.toList());

        // Get previous year data (same period)
        LocalDateTime prevYearStart = start.minusYears(1);
        LocalDateTime prevYearEnd = end.minusYears(1);

        List<Order> previousYearOrders = allOrders.stream()
                .filter(order -> isWithinDateRange(order, prevYearStart, prevYearEnd))
                .collect(Collectors.toList());

        double currentRevenue = currentYearOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum();
        double previousRevenue = previousYearOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum();

        if (previousRevenue == 0) return 0;

        return ((currentRevenue - previousRevenue) / previousRevenue) * 100;
    }

    private double predictNextSeason(Map<Month, Double> revenueByMonth) {
        if (revenueByMonth.isEmpty()) return 0;

        // Simple moving average for prediction
        double averageRevenue = revenueByMonth.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        // Apply seasonal adjustment (assuming 10% growth for next season)
        return averageRevenue * 1.10;
    }

    // Inner classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeasonalBehaviorResult {
        private Map<String, Double> revenueByMonth;
        private Map<String, Long> ordersByMonth;
        private Map<String, Double> revenueByDayOfWeek;
        private Map<String, FestivalAnalysis> festivalAnalysis;
        private Map<String, Double> seasonalTrends;
        private double monthOverMonthGrowth;
        private double yearOverYearGrowth;
        private double nextSeasonForecast;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FestivalAnalysis {
        private double festivalRevenue;
        private long festivalOrders;
        private double regularRevenue;
        private long regularOrders;
        private double averageFestivalOrderValue;
        private double averageRegularOrderValue;
        private double revenueIncreasePercentage;
    }
}