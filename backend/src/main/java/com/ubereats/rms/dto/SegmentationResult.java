package com.ubereats.rms.dto;

import com.ubereats.rms.domain.LoyaltyTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SegmentationResult {
    // Loyalty Tier Analysis
    private Map<LoyaltyTier, Long> customerCountByTier;
    private Map<LoyaltyTier, Double> revenueByTier;
    private Map<LoyaltyTier, Double> averageSpendByTier;

    // Demographic Analysis
    private Map<String, Long> customerCountByGender;
    private Map<String, Long> customerCountByAgeGroup;
    private Map<String, Double> revenueByGender;
    private Map<String, Double> revenueByAgeGroup;

    // Geographic Analysis
    private Map<String, Long> customerCountByBorough;
    private Map<String, Double> revenueByBorough;

    // Behavioral Analysis
    private Map<String, Long> frequencyDistribution; // Orders per customer
    private Map<String, Long> recencyDistribution; // Days since last order
    private Map<String, Long> monetaryDistribution; // Total spend buckets

    // Summary Metrics
    private long totalCustomers;
    private double totalRevenue;
    private double averageCustomerValue;
    private double customerRetentionRate;
    private String topPerformingSegment;

    // Customer Lifetime Value
    private Map<LoyaltyTier, Double> clvByTier;
    private double overallClv;

    public void calculateAverages() {
        if (customerCountByTier != null && revenueByTier != null) {
            averageSpendByTier = new java.util.HashMap<>();
            for (Map.Entry<LoyaltyTier, Long> entry : customerCountByTier.entrySet()) {
                Double revenue = revenueByTier.get(entry.getKey());
                if (revenue != null && entry.getValue() > 0) {
                    averageSpendByTier.put(entry.getKey(), revenue / entry.getValue());
                }
            }
        }

        if (totalCustomers > 0) {
            averageCustomerValue = totalRevenue / totalCustomers;
        }
    }
}