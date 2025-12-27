package com.ubereats.rms.service;

import com.ubereats.rms.domain.Customer;
import com.ubereats.rms.domain.LoyaltyTier;
import com.ubereats.rms.dto.SegmentationResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerSegmentationService {

    public SegmentationResult segmentByLoyalty(List<Customer> customers) {
        Map<LoyaltyTier, Long> countByTier = customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getLoyaltyGroup,
                        Collectors.counting()
                ));

        Map<LoyaltyTier, Double> revenueByTier = customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getLoyaltyGroup,
                        Collectors.summingDouble(Customer::getEstimatedTotalSpentLkr)
                ));

        SegmentationResult result = new SegmentationResult();
        result.setCustomerCountByTier(countByTier);
        result.setRevenueByTier(revenueByTier);

        return result;
    }

    public SegmentationResult segmentByDemographics(List<Customer> customers) {
        Map<String, Long> countByGender = customers.stream()
                .collect(Collectors.groupingBy(
                        customer -> customer.getGender().toString(),
                        Collectors.counting()
                ));

        Map<String, Long> countByAgeGroup = customers.stream()
                .collect(Collectors.groupingBy(
                        this::getAgeGroup,
                        Collectors.counting()
                ));

        SegmentationResult result = new SegmentationResult();
        result.setCustomerCountByGender(countByGender);
        result.setCustomerCountByAgeGroup(countByAgeGroup);

        return result;
    }

    private String getAgeGroup(Customer customer) {
        int age = customer.getAge();
        if (age < 18) return "Under 18";
        if (age < 25) return "18-24";
        if (age < 35) return "25-34";
        if (age < 45) return "35-44";
        if (age < 55) return "45-54";
        return "55+";
    }
}