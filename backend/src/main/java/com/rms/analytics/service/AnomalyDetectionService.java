package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Anomaly Detection
 * Requirement 6: Service anomaly detection
 */
public class AnomalyDetectionService {
    private static final Logger logger = LoggerFactory.getLogger(AnomalyDetectionService.class);
    private static final double ANOMALY_THRESHOLD = 0.5; // 50% deviation threshold

    /**
     * Detect anomalies in order counts
     */
    public AnomalyReport detectOrderCountAnomalies(List<Order> orders) {
        Map<Integer, Integer> hourlyOrders = new HashMap<>();
        
        // Initialize all hours
        for (int i = 0; i < 24; i++) {
            hourlyOrders.put(i, 0);
        }

        // Count orders by hour
        orders.stream()
                .filter(order -> order.getOrderPlaced() != null)
                .forEach(order -> {
                    int hour = order.getOrderPlaced().getHour();
                    hourlyOrders.put(hour, hourlyOrders.getOrDefault(hour, 0) + 1);
                });

        double avgOrders = hourlyOrders.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        List<AnomalyDetail> anomalies = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : hourlyOrders.entrySet()) {
            double deviation = Math.abs(entry.getValue() - avgOrders) / avgOrders;
            if (deviation > ANOMALY_THRESHOLD) {
                anomalies.add(new AnomalyDetail(
                        "Hour " + entry.getKey(),
                        entry.getValue(),
                        (int) avgOrders,
                        deviation * 100
                ));
            }
        }

        return new AnomalyReport("Order Count Anomalies", anomalies.size(), anomalies);
    }

    /**
     * Detect cancellation anomalies
     */
    public AnomalyReport detectCancellationAnomalies(List<Order> orders) {
        Map<String, Integer> outletCancellations = new HashMap<>();
        Map<String, Integer> outletTotals = new HashMap<>();

        for (Order order : orders) {
            String outletId = order.getOutletId();
            outletTotals.put(outletId, outletTotals.getOrDefault(outletId, 0) + 1);
            
            if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
                outletCancellations.put(outletId, outletCancellations.getOrDefault(outletId, 0) + 1);
            }
        }

        double avgCancellationRate = outletCancellations.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        List<AnomalyDetail> anomalies = new ArrayList<>();
        for (String outletId : outletTotals.keySet()) {
            int cancelled = outletCancellations.getOrDefault(outletId, 0);
            int total = outletTotals.get(outletId);
            double rate = (double) cancelled / total * 100;
            
            if (Math.abs(rate - avgCancellationRate) > ANOMALY_THRESHOLD * avgCancellationRate) {
                anomalies.add(new AnomalyDetail(
                        "Outlet " + outletId,
                        cancelled,
                        (int) avgCancellationRate,
                        rate - avgCancellationRate
                ));
            }
        }

        return new AnomalyReport("Cancellation Anomalies", anomalies.size(), anomalies);
    }

    /**
     * Detect revenue anomalies
     */
    public AnomalyReport detectRevenueAnomalies(List<Order> orders) {
        Map<Integer, Double> hourlyRevenue = new HashMap<>();
        
        for (int i = 0; i < 24; i++) {
            hourlyRevenue.put(i, 0.0);
        }

        orders.stream()
                .filter(order -> order.getOrderPlaced() != null && "Completed".equalsIgnoreCase(order.getStatus()))
                .forEach(order -> {
                    int hour = order.getOrderPlaced().getHour();
                    hourlyRevenue.put(hour, hourlyRevenue.getOrDefault(hour, 0.0) + order.getTotalPrice());
                });

        double avgRevenue = hourlyRevenue.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        List<AnomalyDetail> anomalies = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : hourlyRevenue.entrySet()) {
            if (avgRevenue > 0) {
                double deviation = Math.abs(entry.getValue() - avgRevenue) / avgRevenue;
                if (deviation > ANOMALY_THRESHOLD) {
                    anomalies.add(new AnomalyDetail(
                            "Hour " + entry.getKey(),
                            (int) (double) entry.getValue(),
                            (int) avgRevenue,
                            deviation * 100
                    ));
                }
            }
        }

        return new AnomalyReport("Revenue Anomalies", anomalies.size(), anomalies);
    }

    // DTOs for response
    public static class AnomalyReport {
        public String reportType;
        public int anomalyCount;
        public List<AnomalyDetail> anomalies;

        public AnomalyReport(String reportType, int count, List<AnomalyDetail> anomalies) {
            this.reportType = reportType;
            this.anomalyCount = count;
            this.anomalies = anomalies;
        }
    }

    public static class AnomalyDetail {
        public String dimension;
        public int actualValue;
        public int expectedValue;
        public double deviationPercent;

        public AnomalyDetail(String dimension, int actual, int expected, double deviation) {
            this.dimension = dimension;
            this.actualValue = actual;
            this.expectedValue = expected;
            this.deviationPercent = deviation;
        }
    }
}
