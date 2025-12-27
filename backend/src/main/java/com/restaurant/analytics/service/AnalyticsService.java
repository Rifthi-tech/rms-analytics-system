package com.restaurant.analytics.service;

import com.restaurant.analytics.model.*;
import com.restaurant.analytics.repository.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private DataLoader dataLoader;

    public Map<String, Object> getPeakDiningAnalysis() {
        List<Order> orders = dataLoader.loadOrders();

        Map<Integer, Long> hourCounts = orders.stream()
                .filter(o -> o.getOrderPlaced() != null)
                .collect(Collectors.groupingBy(o -> o.getOrderPlaced().getHour(), Collectors.counting()));

        Map<String, Long> dayCounts = orders.stream()
                .filter(o -> o.getOrderPlaced() != null)
                .collect(Collectors.groupingBy(o -> o.getOrderPlaced().getDayOfWeek().toString(), Collectors.counting()));

        Map<Integer, Long> monthCounts = orders.stream()
                .filter(o -> o.getOrderPlaced() != null)
                .collect(Collectors.groupingBy(o -> o.getOrderPlaced().getMonthValue(), Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("hourly", hourCounts);
        result.put("daily", dayCounts);
        result.put("monthly", monthCounts);
        result.put("peakHour", Collections.max(hourCounts.entrySet(), Map.Entry.comparingByValue()).getKey());
        result.put("peakDay", Collections.max(dayCounts.entrySet(), Map.Entry.comparingByValue()).getKey());

        return result;
    }

    public Map<String, Object> getCustomerSegmentation() {
        List<Order> orders = dataLoader.loadOrders();

        Map<String, Long> genderDist = orders.stream()
                .filter(o -> o.getGender() != null && !o.getGender().isEmpty())
                .collect(Collectors.groupingBy(Order::getGender, Collectors.counting()));

        Map<String, Long> ageDist = orders.stream()
                .filter(o -> o.getAge() != null)
                .collect(Collectors.groupingBy(o -> getAgeGroup(o.getAge()), Collectors.counting()));

        Map<String, Long> loyaltyDist = orders.stream()
                .filter(o -> o.getLoyaltyGroup() != null && !o.getLoyaltyGroup().isEmpty())
                .collect(Collectors.groupingBy(Order::getLoyaltyGroup, Collectors.counting()));

        Map<String, Double> loyaltySpending = orders.stream()
                .filter(o -> o.getLoyaltyGroup() != null && o.getEstimatedTotalSpentLkr() != null)
                .collect(Collectors.groupingBy(Order::getLoyaltyGroup,
                        Collectors.averagingDouble(Order::getEstimatedTotalSpentLkr)));

        Map<String, Object> result = new HashMap<>();
        result.put("genderDistribution", genderDist);
        result.put("ageDistribution", ageDist);
        result.put("loyaltyDistribution", loyaltyDist);
        result.put("loyaltyAvgSpending", loyaltySpending);

        return result;
    }

    public Map<String, Object> getSeasonalBehavior() {
        List<Order> orders = dataLoader.loadOrders();

        Map<String, Double> monthlyRevenue = orders.stream()
                .filter(o -> o.getOrderPlaced() != null && o.getTotalPriceLkr() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getOrderPlaced().getMonth().toString(),
                        Collectors.summingDouble(Order::getTotalPriceLkr)
                ));

        Map<String, Long> monthlyOrders = orders.stream()
                .filter(o -> o.getOrderPlaced() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getOrderPlaced().getMonth().toString(),
                        Collectors.counting()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("monthlyRevenue", monthlyRevenue);
        result.put("monthlyOrders", monthlyOrders);

        return result;
    }

    public Map<String, Object> getMenuItemAnalysis() {
        List<Order> orders = dataLoader.loadOrders();

        Map<String, Long> topItems = orders.stream()
                .filter(o -> o.getName() != null && !o.getName().isEmpty())
                .collect(Collectors.groupingBy(Order::getName, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<String, Double> itemRevenue = orders.stream()
                .filter(o -> o.getName() != null && o.getTotalPriceLkr() != null)
                .collect(Collectors.groupingBy(Order::getName, Collectors.summingDouble(Order::getTotalPriceLkr)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<String, Long> categoryDist = orders.stream()
                .filter(o -> o.getCategory() != null && !o.getCategory().isEmpty())
                .collect(Collectors.groupingBy(Order::getCategory, Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("topItems", topItems);
        result.put("itemRevenue", itemRevenue);
        result.put("categoryDistribution", categoryDist);

        return result;
    }

    public Map<String, Object> getRevenueAnalysis() {
        List<Order> orders = dataLoader.loadOrders();

        double totalRevenue = orders.stream()
                .filter(o -> o.getTotalPriceLkr() != null)
                .mapToDouble(Order::getTotalPriceLkr)
                .sum();

        Map<String, Double> paymentMethodRevenue = orders.stream()
                .filter(o -> o.getPaymentMethod() != null && o.getTotalPriceLkr() != null)
                .collect(Collectors.groupingBy(Order::getPaymentMethod, Collectors.summingDouble(Order::getTotalPriceLkr)));

        double avgOrderValue = orders.stream()
                .filter(o -> o.getTotalPriceLkr() != null)
                .mapToDouble(Order::getTotalPriceLkr)
                .average()
                .orElse(0.0);

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("paymentMethodRevenue", paymentMethodRevenue);
        result.put("avgOrderValue", avgOrderValue);
        result.put("totalOrders", orders.size());

        return result;
    }

    public Map<String, Object> getBranchPerformance() {
        List<Order> orders = dataLoader.loadOrders();

        Map<String, Long> branchOrders = orders.stream()
                .filter(o -> o.getNameY() != null && !o.getNameY().isEmpty())
                .collect(Collectors.groupingBy(Order::getNameY, Collectors.counting()));

        Map<String, Double> branchRevenue = orders.stream()
                .filter(o -> o.getNameY() != null && o.getTotalPriceLkr() != null)
                .collect(Collectors.groupingBy(Order::getNameY, Collectors.summingDouble(Order::getTotalPriceLkr)));

        Map<String, Double> branchAvgOrder = orders.stream()
                .filter(o -> o.getNameY() != null && o.getTotalPriceLkr() != null)
                .collect(Collectors.groupingBy(Order::getNameY, Collectors.averagingDouble(Order::getTotalPriceLkr)));

        Map<String, Object> result = new HashMap<>();
        result.put("branchOrders", branchOrders);
        result.put("branchRevenue", branchRevenue);
        result.put("branchAvgOrder", branchAvgOrder);

        return result;
    }

    public Map<String, Object> getAnomalyDetection() {
        List<Order> orders = dataLoader.loadOrders();

        Map<String, Long> dailyOrders = orders.stream()
                .filter(o -> o.getOrderPlaced() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getOrderPlaced().toLocalDate().toString(),
                        Collectors.counting()
                ));

        double avgDaily = dailyOrders.values().stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = Math.sqrt(dailyOrders.values().stream()
                .mapToDouble(v -> Math.pow(v - avgDaily, 2))
                .average().orElse(0));

        Map<String, Long> anomalies = dailyOrders.entrySet().stream()
                .filter(e -> Math.abs(e.getValue() - avgDaily) > 2 * stdDev)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Object> result = new HashMap<>();
        result.put("averageDaily", avgDaily);
        result.put("stdDeviation", stdDev);
        result.put("anomalyDays", anomalies);

        return result;
    }

    private String getAgeGroup(Integer age) {
        if (age < 20) return "Under 20";
        if (age < 30) return "20-29";
        if (age < 40) return "30-39";
        if (age < 50) return "40-49";
        return "50+";
    }
}