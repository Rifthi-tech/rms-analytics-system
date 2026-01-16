package com.rms.analytics.service;

import com.rms.analytics.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Customer Demographics and Segmentation
 * Requirement 2: Analyze customer demographics and loyalty segmentation
 */
public class CustomerDemographicsService {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CustomerDemographicsService.class);

    /**
     * Analyze demographics by gender
     */
    public Map<String, Integer> analyzeByGender(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getGender,
                        Collectors.summingInt(c -> 1)
                ));
    }

    /**
     * Analyze demographics by age group
     */
    public Map<String, Integer> analyzeByAgeGroup(List<Customer> customers) {
        Map<String, Integer> ageGroups = new HashMap<>();
        ageGroups.put("10-20", 0);
        ageGroups.put("21-30", 0);
        ageGroups.put("31-40", 0);
        ageGroups.put("41-50", 0);
        ageGroups.put("51-60", 0);
        ageGroups.put("60+", 0);

        for (Customer customer : customers) {
            int age = customer.getAge();
            if (age >= 10 && age <= 20) ageGroups.put("10-20", ageGroups.get("10-20") + 1);
            else if (age >= 21 && age <= 30) ageGroups.put("21-30", ageGroups.get("21-30") + 1);
            else if (age >= 31 && age <= 40) ageGroups.put("31-40", ageGroups.get("31-40") + 1);
            else if (age >= 41 && age <= 50) ageGroups.put("41-50", ageGroups.get("41-50") + 1);
            else if (age >= 51 && age <= 60) ageGroups.put("51-60", ageGroups.get("51-60") + 1);
            else ageGroups.put("60+", ageGroups.get("60+") + 1);
        }

        return ageGroups;
    }

    /**
     * Segment customers by loyalty group
     */
    public Map<String, List<Customer>> segmentByLoyalty(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.groupingBy(Customer::getLoyaltyGroup));
    }

    /**
     * Get loyalty statistics
     */
    public LoyaltySegmentationStats getLoyaltyStats(List<Customer> customers) {
        Map<String, List<Customer>> segments = segmentByLoyalty(customers);
        
        Map<String, Integer> segmentCounts = segments.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()
                ));

        Map<String, Double> avgSpendingBySegment = segments.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(Customer::getEstimatedTotalSpent)
                                .average()
                                .orElse(0.0)
                ));

        return new LoyaltySegmentationStats(segmentCounts, avgSpendingBySegment);
    }

    // DTO for response
    public static class LoyaltySegmentationStats {
        public Map<String, Integer> segmentCounts;
        public Map<String, Double> avgSpendingBySegment;

        public LoyaltySegmentationStats(Map<String, Integer> counts, Map<String, Double> spending) {
            this.segmentCounts = counts;
            this.avgSpendingBySegment = spending;
        }
    }
}
