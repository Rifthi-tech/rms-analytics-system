package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Branch Performance Analysis
 * Requirement 7: Branch performance evaluation and ranking
 */
public class BranchPerformanceService {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(BranchPerformanceService.class);

    /**
     * Get performance metrics by branch
     */
    public Map<String, BranchPerformance> analyzeBranchPerformance(List<Order> orders) {
        Map<String, List<Order>> ordersByBranch = orders.stream()
                .collect(Collectors.groupingBy(Order::getOutletId));

        return ordersByBranch.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateBranchMetrics(entry.getKey(), entry.getValue())
                ));
    }

    /**
     * Calculate metrics for a single branch
     */
    private BranchPerformance calculateBranchMetrics(String branchId, List<Order> orders) {
        long totalOrders = orders.stream()
                .filter(o -> "Completed".equalsIgnoreCase(o.getStatus()))
                .count();

        double totalRevenue = orders.stream()
                .filter(o -> "Completed".equalsIgnoreCase(o.getStatus()))
                .mapToDouble(Order::getTotalPrice)
                .sum();

        long cancelledOrders = orders.stream()
                .filter(o -> "Cancelled".equalsIgnoreCase(o.getStatus()))
                .count();

        double completionRate = totalOrders > 0 ? (double) totalOrders / (totalOrders + cancelledOrders) * 100 : 0;
        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;

        return new BranchPerformance(
                branchId,
                totalOrders,
                totalRevenue,
                cancelledOrders,
                completionRate,
                avgOrderValue
        );
    }

    /**
     * Rank branches by performance
     */
    public List<BranchPerformance> rankBranchesByPerformance(List<Order> orders, String metric) {
        Map<String, BranchPerformance> performance = analyzeBranchPerformance(orders);

        return performance.values().stream()
                .sorted((b1, b2) -> {
                    switch (metric.toLowerCase()) {
                        case "revenue":
                            return Double.compare(b2.totalRevenue, b1.totalRevenue);
                        case "orders":
                            return Long.compare(b2.totalOrders, b1.totalOrders);
                        case "completionrate":
                            return Double.compare(b2.completionRate, b1.completionRate);
                        case "avgordervalue":
                            return Double.compare(b2.avgOrderValue, b1.avgOrderValue);
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Identify underperforming branches
     */
    public List<BranchPerformance> identifyUnderperformers(List<Order> orders) {
        Map<String, BranchPerformance> performance = analyzeBranchPerformance(orders);

        double avgCompletionRate = performance.values().stream()
                .mapToDouble(b -> b.completionRate)
                .average()
                .orElse(0.0);

        return performance.values().stream()
                .filter(branch -> branch.completionRate < avgCompletionRate)
                .sorted(Comparator.comparingDouble(b -> b.completionRate))
                .collect(Collectors.toList());
    }

    // DTO for response
    public static class BranchPerformance {
        public String branchId;
        public long totalOrders;
        public double totalRevenue;
        public long cancelledOrders;
        public double completionRate;
        public double avgOrderValue;

        public BranchPerformance(String branchId, long totalOrders, double totalRevenue,
                                long cancelledOrders, double completionRate, double avgOrderValue) {
            this.branchId = branchId;
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.cancelledOrders = cancelledOrders;
            this.completionRate = completionRate;
            this.avgOrderValue = avgOrderValue;
        }
    }
}
