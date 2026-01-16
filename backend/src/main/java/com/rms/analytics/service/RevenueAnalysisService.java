package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Revenue and Ticket Analysis
 * Requirement 5: Ticket counting and revenue analysis
 */
public class RevenueAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(RevenueAnalysisService.class);

    /**
     * Calculate total revenue across all outlets
     */
    public double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    /**
     * Get revenue by outlet
     */
    public Map<String, Double> getRevenueByOutlet(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(
                        Order::getOutletId,
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    /**
     * Get revenue by date
     */
    public Map<LocalDate, Double> getRevenueByDate(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getOrderPlaced() != null && "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().toLocalDate(),
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    /**
     * Get daily sales summary
     */
    public DailySalesSummary getDailySalesSummary(List<Order> orders) {
        Map<LocalDate, Double> dailyRevenue = getRevenueByDate(orders);
        Map<LocalDate, Integer> dailyOrders = orders.stream()
                .filter(order -> order.getOrderPlaced() != null && "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().toLocalDate(),
                        Collectors.summingInt(order -> 1)
                ));

        double avgDailyRevenue = dailyRevenue.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new DailySalesSummary(dailyRevenue, dailyOrders, avgDailyRevenue);
    }

    /**
     * Get revenue by payment method
     */
    public Map<String, Double> getRevenueByPaymentMethod(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(
                        Order::getPaymentMethod,
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    /**
     * Count total orders
     */
    public long countTotalOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .count();
    }

    /**
     * Count orders by outlet
     */
    public Map<String, Long> countOrdersByOutlet(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(
                        Order::getOutletId,
                        Collectors.counting()
                ));
    }

    // DTO for response
    public static class DailySalesSummary {
        public Map<LocalDate, Double> dailyRevenue;
        public Map<LocalDate, Integer> dailyOrders;
        public double avgDailyRevenue;

        public DailySalesSummary(Map<LocalDate, Double> revenue, Map<LocalDate, Integer> orders, double avgRevenue) {
            this.dailyRevenue = revenue;
            this.dailyOrders = orders;
            this.avgDailyRevenue = avgRevenue;
        }
    }
}
