package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Peak Dining Analysis
 * Requirement 1: Identify peak hours, days, and months
 */
public class PeakDiningAnalysisService {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(PeakDiningAnalysisService.class);

    /**
     * Analyze peak hours from orders
     */
    public Map<Integer, Integer> analyzeHourlyPeaks(List<Order> orders) {
        Map<Integer, Integer> hourlyData = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            hourlyData.put(i, 0);
        }

        orders.stream()
                .filter(order -> order.getOrderPlaced() != null)
                .forEach(order -> {
                    int hour = order.getOrderPlaced().getHour();
                    hourlyData.put(hour, hourlyData.get(hour) + 1);
                });

        return hourlyData;
    }

    /**
     * Analyze peak days from orders
     */
    public Map<Integer, Integer> analyzeDailyPeaks(List<Order> orders) {
        Map<Integer, Integer> dailyData = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            dailyData.put(i, 0);
        }

        orders.stream()
                .filter(order -> order.getOrderPlaced() != null)
                .forEach(order -> {
                    int dayOfWeek = order.getOrderPlaced().getDayOfWeek().getValue();
                    dailyData.put(dayOfWeek, dailyData.getOrDefault(dayOfWeek, 0) + 1);
                });

        return dailyData;
    }

    /**
     * Analyze peak months from orders
     */
    public Map<Integer, Integer> analyzeMonthlyPeaks(List<Order> orders) {
        Map<Integer, Integer> monthlyData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyData.put(i, 0);
        }

        orders.stream()
                .filter(order -> order.getOrderPlaced() != null)
                .forEach(order -> {
                    int month = order.getOrderPlaced().getMonthValue();
                    monthlyData.put(month, monthlyData.getOrDefault(month, 0) + 1);
                });

        return monthlyData;
    }

    /**
     * Get peak hour analysis with statistics
     */
    public PeakHourAnalysis getPeakHourAnalysis(List<Order> orders) {
        Map<Integer, Integer> hourlyPeaks = analyzeHourlyPeaks(orders);
        
        Integer peakHour = hourlyPeaks.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);

        Integer peakCount = hourlyPeaks.get(peakHour);

        return new PeakHourAnalysis(hourlyPeaks, peakHour, peakCount);
    }

    /**
     * Get branch-level summary
     */
    public Map<String, BranchPeakData> getBranchPeakSummary(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getOutletId))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<Order> branchOrders = entry.getValue();
                            Map<Integer, Integer> hourlyPeaks = analyzeHourlyPeaks(branchOrders);
                            Integer peakHour = hourlyPeaks.entrySet().stream()
                                    .max(Map.Entry.comparingByValue())
                                    .map(Map.Entry::getKey)
                                    .orElse(0);
                            return new BranchPeakData(
                                    entry.getKey(),
                                    branchOrders.size(),
                                    peakHour,
                                    hourlyPeaks.get(peakHour)
                            );
                        }
                ));
    }

    // DTOs for response
    public static class PeakHourAnalysis {
        public Map<Integer, Integer> hourlyData;
        public Integer peakHour;
        public Integer orderCount;

        public PeakHourAnalysis(Map<Integer, Integer> hourlyData, Integer peakHour, Integer orderCount) {
            this.hourlyData = hourlyData;
            this.peakHour = peakHour;
            this.orderCount = orderCount;
        }
    }

    public static class BranchPeakData {
        public String branchId;
        public Integer totalOrders;
        public Integer peakHour;
        public Integer peakOrderCount;

        public BranchPeakData(String branchId, Integer totalOrders, Integer peakHour, Integer peakOrderCount) {
            this.branchId = branchId;
            this.totalOrders = totalOrders;
            this.peakHour = peakHour;
            this.peakOrderCount = peakOrderCount;
        }
    }
}
