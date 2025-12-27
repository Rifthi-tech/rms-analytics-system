package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.domain.Outlet;
import com.ubereats.rms.dto.BranchPerformanceResult;
import com.ubereats.rms.exception.AnalysisException;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BranchPerformanceService {

    @Autowired
    private OrderRepository orderRepository;

    public BranchPerformanceResult analyzeBranchPerformance(LocalDateTime startDate, LocalDateTime endDate,
                                                            String region, String performanceMetric) {
        try {
            List<Order> orders = orderRepository.findAll();

            List<Order> filteredOrders = orders.stream()
                    .filter(order -> isWithinDateRange(order, startDate, endDate))
                    .collect(Collectors.toList());

            BranchPerformanceResult result = new BranchPerformanceResult();

            // Group orders by outlet
            Map<String, List<Order>> ordersByOutlet = filteredOrders.stream()
                    .collect(Collectors.groupingBy(Order::getOutletId));

            // Calculate performance metrics for each outlet
            Map<String, OutletMetrics> outletMetrics = new HashMap<>();

            for (Map.Entry<String, List<Order>> entry : ordersByOutlet.entrySet()) {
                String outletId = entry.getKey();
                List<Order> outletOrders = entry.getValue();

                OutletMetrics metrics = calculateOutletMetrics(outletOrders);
                outletMetrics.put(outletId, metrics);
            }

            result.setOutletMetrics(outletMetrics);

            // Rank outlets based on selected metric
            Map<String, Integer> rankings = rankOutlets(outletMetrics, performanceMetric);
            result.setOutletRankings(rankings);

            // Calculate regional performance if region specified
            if (region != null && !region.isEmpty()) {
                Map<String, Double> regionalPerformance = calculateRegionalPerformance(outletMetrics, region);
                result.setRegionalPerformance(regionalPerformance);
            }

            // Identify top and bottom performers
            result.setTopPerformers(identifyTopPerformers(outletMetrics, performanceMetric, 5));
            result.setBottomPerformers(identifyBottomPerformers(outletMetrics, performanceMetric, 5));

            // Calculate efficiency metrics
            result.setEfficiencyAnalysis(calculateEfficiencyMetrics(outletMetrics));

            // Generate performance insights
            result.setPerformanceInsights(generatePerformanceInsights(outletMetrics, rankings));

            // Calculate growth metrics
            result.setGrowthMetrics(calculateGrowthMetrics(orders, startDate, endDate));

            return result;

        } catch (Exception e) {
            throw new AnalysisException("BRANCH_PERFORMANCE", "BranchPerformanceService",
                    "Failed to analyze branch performance", e);
        }
    }

    private boolean isWithinDateRange(Order order, LocalDateTime start, LocalDateTime end) {
        return order.getOrderPlaced() != null &&
                !order.getOrderPlaced().isBefore(start) &&
                !order.getOrderPlaced().isAfter(end);
    }

    private OutletMetrics calculateOutletMetrics(List<Order> outletOrders) {
        OutletMetrics metrics = new OutletMetrics();

        metrics.setTotalOrders(outletOrders.size());
        metrics.setTotalRevenue(outletOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum());
        metrics.setAverageOrderValue(metrics.getTotalRevenue() / Math.max(1, outletOrders.size()));

        // Calculate completion rate
        long completedOrders = outletOrders.stream()
                .filter(order -> order.getStatus().isCompleted())
                .count();
        metrics.setCompletionRate((double) completedOrders / Math.max(1, outletOrders.size()) * 100);

        // Calculate cancellation rate
        long cancelledOrders = outletOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .count();
        metrics.setCancellationRate((double) cancelledOrders / Math.max(1, outletOrders.size()) * 100);

        // Calculate average preparation time
        double avgPrepTime = outletOrders.stream()
                .filter(order -> order.getPrepStarted() != null && order.getPrepFinished() != null)
                .mapToLong(order -> java.time.Duration.between(
                        order.getPrepStarted(), order.getPrepFinished()).toMinutes())
                .average()
                .orElse(0);
        metrics.setAveragePreparationTime(avgPrepTime);

        // Calculate items per order
        double avgItems = outletOrders.stream()
                .mapToDouble(Order::getNumItems)
                .average()
                .orElse(0);
        metrics.setAverageItemsPerOrder(avgItems);

        // Calculate peak hour performance
        Map<Integer, Long> ordersByHour = outletOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getHour(),
                        Collectors.counting()
                ));
        metrics.setPeakHour(ordersByHour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1));

        // Calculate customer satisfaction score (simplified)
        // In real implementation, this would come from reviews/feedback
        metrics.setCustomerSatisfactionScore(calculateSatisfactionScore(outletOrders));

        return metrics;
    }

    private double calculateSatisfactionScore(List<Order> orders) {
        // Simplified satisfaction calculation based on order metrics
        double score = 70.0; // Base score

        // Adjust based on cancellation rate
        long totalOrders = orders.size();
        long cancelledOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .count();
        double cancellationRate = (double) cancelledOrders / Math.max(1, totalOrders);
        score -= cancellationRate * 100 * 20; // Deduct up to 20 points for cancellations

        // Adjust based on order value (higher values might indicate satisfaction)
        double avgOrderValue = orders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .average()
                .orElse(0);
        if (avgOrderValue > 1000) score += 5;
        if (avgOrderValue > 2000) score += 5;

        return Math.max(0, Math.min(100, score));
    }

    private Map<String, Integer> rankOutlets(Map<String, OutletMetrics> outletMetrics, String metric) {
        List<Map.Entry<String, OutletMetrics>> sortedOutlets;

        switch (metric.toUpperCase()) {
            case "REVENUE":
                sortedOutlets = outletMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(
                                e2.getValue().getTotalRevenue(),
                                e1.getValue().getTotalRevenue()))
                        .collect(Collectors.toList());
                break;

            case "ORDERS":
                sortedOutlets = outletMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(
                                e2.getValue().getTotalOrders(),
                                e1.getValue().getTotalOrders()))
                        .collect(Collectors.toList());
                break;

            case "AVERAGE_ORDER_VALUE":
                sortedOutlets = outletMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(
                                e2.getValue().getAverageOrderValue(),
                                e1.getValue().getAverageOrderValue()))
                        .collect(Collectors.toList());
                break;

            case "SATISFACTION":
                sortedOutlets = outletMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(
                                e2.getValue().getCustomerSatisfactionScore(),
                                e1.getValue().getCustomerSatisfactionScore()))
                        .collect(Collectors.toList());
                break;

            default:
                sortedOutlets = outletMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(
                                e2.getValue().getTotalRevenue(),
                                e1.getValue().getTotalRevenue()))
                        .collect(Collectors.toList());
        }

        Map<String, Integer> rankings = new HashMap<>();
        for (int i = 0; i < sortedOutlets.size(); i++) {
            rankings.put(sortedOutlets.get(i).getKey(), i + 1);
        }

        return rankings;
    }

    private Map<String, Double> calculateRegionalPerformance(Map<String, OutletMetrics> outletMetrics, String region) {
        // In real implementation, this would group outlets by region
        // For now, return overall averages
        Map<String, Double> regionalMetrics = new HashMap<>();

        double totalRevenue = outletMetrics.values().stream()
                .mapToDouble(OutletMetrics::getTotalRevenue)
                .sum();
        double totalOrders = outletMetrics.values().stream()
                .mapToDouble(OutletMetrics::getTotalOrders)
                .sum();
        double avgSatisfaction = outletMetrics.values().stream()
                .mapToDouble(OutletMetrics::getCustomerSatisfactionScore)
                .average()
                .orElse(0);

        regionalMetrics.put("totalRevenue", totalRevenue);
        regionalMetrics.put("totalOrders", totalOrders);
        regionalMetrics.put("averageSatisfaction", avgSatisfaction);
        regionalMetrics.put("outletCount", (double) outletMetrics.size());

        return regionalMetrics;
    }

    private List<String> identifyTopPerformers(Map<String, OutletMetrics> outletMetrics, String metric, int limit) {
        return rankOutlets(outletMetrics, metric).entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<String> identifyBottomPerformers(Map<String, OutletMetrics> outletMetrics, String metric, int limit) {
        List<Map.Entry<String, Integer>> rankings = new ArrayList<>(rankOutlets(outletMetrics, metric).entrySet());
        rankings.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return rankings.stream()
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<String, Double> calculateEfficiencyMetrics(Map<String, OutletMetrics> outletMetrics) {
        Map<String, Double> efficiency = new HashMap<>();

        // Calculate revenue per hour (efficiency)
        for (Map.Entry<String, OutletMetrics> entry : outletMetrics.entrySet()) {
            double revenuePerHour = entry.getValue().getTotalRevenue() /
                    Math.max(1, entry.getValue().getAveragePreparationTime()) * 60;
            efficiency.put(entry.getKey(), revenuePerHour);
        }

        return efficiency;
    }

    private List<String> generatePerformanceInsights(Map<String, OutletMetrics> outletMetrics,
                                                     Map<String, Integer> rankings) {
        List<String> insights = new ArrayList<>();

        // Identify highest revenue outlet
        String topRevenueOutlet = rankings.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        insights.add(String.format("Top performing outlet by revenue: %s", topRevenueOutlet));

        // Identify outlets with high cancellation rates
        List<String> highCancellationOutlets = outletMetrics.entrySet().stream()
                .filter(entry -> entry.getValue().getCancellationRate() > 10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!highCancellationOutlets.isEmpty()) {
            insights.add(String.format("Outlets with high cancellation rates (>10%%): %s",
                    String.join(", ", highCancellationOutlets)));
        }

        // Identify outlets with low satisfaction
        List<String> lowSatisfactionOutlets = outletMetrics.entrySet().stream()
                .filter(entry -> entry.getValue().getCustomerSatisfactionScore() < 60)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!lowSatisfactionOutlets.isEmpty()) {
            insights.add(String.format("Outlets with low satisfaction scores (<60): %s",
                    String.join(", ", lowSatisfactionOutlets)));
        }

        return insights;
    }

    private Map<String, Double> calculateGrowthMetrics(List<Order> allOrders, LocalDateTime start, LocalDateTime end) {
        Map<String, Double> growthMetrics = new HashMap<>();

        // Calculate previous period for comparison
        long daysBetween = java.time.Duration.between(start, end).toDays();
        LocalDateTime prevStart = start.minusDays(daysBetween);
        LocalDateTime prevEnd = start;

        // Current period metrics
        List<Order> currentOrders = allOrders.stream()
                .filter(order -> isWithinDateRange(order, start, end))
                .collect(Collectors.toList());

        // Previous period metrics
        List<Order> previousOrders = allOrders.stream()
                .filter(order -> isWithinDateRange(order, prevStart, prevEnd))
                .collect(Collectors.toList());

        double currentRevenue = currentOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum();
        double previousRevenue = previousOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum();

        long currentOrdersCount = currentOrders.size();
        long previousOrdersCount = previousOrders.size();

        // Calculate growth rates
        double revenueGrowth = previousRevenue > 0 ?
                ((currentRevenue - previousRevenue) / previousRevenue) * 100 : 0;
        double ordersGrowth = previousOrdersCount > 0 ?
                ((currentOrdersCount - previousOrdersCount) / (double) previousOrdersCount) * 100 : 0;

        growthMetrics.put("revenueGrowth", revenueGrowth);
        growthMetrics.put("ordersGrowth", ordersGrowth);
        growthMetrics.put("currentRevenue", currentRevenue);
        growthMetrics.put("previousRevenue", previousRevenue);

        return growthMetrics;
    }

    // Inner classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchPerformanceResult {
        private Map<String, OutletMetrics> outletMetrics;
        private Map<String, Integer> outletRankings;
        private Map<String, Double> regionalPerformance;
        private List<String> topPerformers;
        private List<String> bottomPerformers;
        private Map<String, Double> efficiencyAnalysis;
        private List<String> performanceInsights;
        private Map<String, Double> growthMetrics;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutletMetrics {
        private String outletId;
        private String outletName;
        private int totalOrders;
        private double totalRevenue;
        private double averageOrderValue;
        private double completionRate; // Percentage
        private double cancellationRate; // Percentage
        private double averagePreparationTime; // Minutes
        private double averageItemsPerOrder;
        private int peakHour; // 0-23
        private double customerSatisfactionScore; // 0-100
        private double efficiencyScore; // Custom score

        public double getRevenuePerOrder() {
            return totalOrders > 0 ? totalRevenue / totalOrders : 0;
        }

        public String getPerformanceCategory() {
            if (customerSatisfactionScore >= 80 && cancellationRate < 5) {
                return "EXCELLENT";
            } else if (customerSatisfactionScore >= 60 && cancellationRate < 10) {
                return "GOOD";
            } else if (customerSatisfactionScore >= 40) {
                return "FAIR";
            } else {
                return "NEEDS_IMPROVEMENT";
            }
        }
    }
}