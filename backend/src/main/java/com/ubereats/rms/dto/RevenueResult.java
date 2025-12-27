package com.ubereats.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResult {
    // Overall Metrics
    private double totalRevenue;
    private double totalOrders;
    private double averageOrderValue;
    private double revenueGrowthRate;

    // Time-based Analysis
    private Map<Integer, Double> revenueByHour;
    private Map<String, Double> revenueByDay;
    private Map<String, Double> revenueByMonth;
    private Map<String, Double> revenueByQuarter;

    // Payment Method Analysis
    private Map<String, Double> revenueByPaymentMethod;
    private Map<String, Long> ordersByPaymentMethod;

    // Outlet Performance
    private Map<String, Double> revenueByOutlet;
    private Map<String, Double> averageOrderValueByOutlet;
    private Map<String, Long> ordersByOutlet;

    // Category Performance
    private Map<String, Double> revenueByCategory;
    private Map<String, Long> ordersByCategory;

    // Status Analysis
    private Map<String, Double> revenueByStatus;
    private Map<String, Long> ordersByStatus;

    // Customer Tier Analysis
    private Map<String, Double> revenueByCustomerTier;

    // Reconciliation Metrics
    private double totalPaidAmount;
    private double totalRefundedAmount;
    private double totalCancelledAmount;
    private double netRevenue;
    private double reconciliationRate;

    // Peak Performance
    private String peakRevenueHour;
    private String peakRevenueDay;
    private String peakRevenueOutlet;

    // Forecast (if applicable)
    private double nextMonthForecast;
    private double forecastConfidence;

    public void calculateNetRevenue() {
        this.netRevenue = totalPaidAmount - totalRefundedAmount;
        if (totalPaidAmount > 0) {
            this.reconciliationRate = (netRevenue / totalPaidAmount) * 100;
        }
    }

    public void identifyPeakMetrics() {
        if (revenueByHour != null && !revenueByHour.isEmpty()) {
            this.peakRevenueHour = revenueByHour.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> String.format("%02d:00", entry.getKey()))
                    .orElse("N/A");
        }

        if (revenueByOutlet != null && !revenueByOutlet.isEmpty()) {
            this.peakRevenueOutlet = revenueByOutlet.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
        }
    }
}