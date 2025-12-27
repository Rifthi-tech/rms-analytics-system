package com.ubereats.rms.service;

import com.ubereats.rms.domain.MenuItem;
import com.ubereats.rms.domain.Order;
import com.ubereats.rms.domain.OrderItem;
import com.ubereats.rms.dto.MenuAnalysisResult;
import com.ubereats.rms.exception.AnalysisException;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuAnalysisService {

    @Autowired
    private OrderRepository orderRepository;

    public MenuAnalysisResult analyzeMenuItems(LocalDateTime startDate, LocalDateTime endDate, String outletId) {
        try {
            List<Order> orders = orderRepository.findAll();

            // Filter orders based on date range and outlet
            List<Order> filteredOrders = orders.stream()
                    .filter(order -> isWithinDateRange(order, startDate, endDate))
                    .filter(order -> outletId == null || outletId.isEmpty() ||
                            outletId.equals(order.getOutletId()))
                    .collect(Collectors.toList());

            MenuAnalysisResult result = new MenuAnalysisResult();

            // Extract all order items
            List<OrderItem> allItems = filteredOrders.stream()
                    .flatMap(order -> order.getItems().stream())
                    .collect(Collectors.toList());

            // Analyze top selling items
            Map<String, Long> itemFrequency = allItems.stream()
                    .collect(Collectors.groupingBy(
                            OrderItem::getItemId,
                            Collectors.summingLong(OrderItem::getQuantity)
                    ));

            Map<String, Double> itemRevenue = allItems.stream()
                    .collect(Collectors.groupingBy(
                            OrderItem::getItemId,
                            Collectors.summingDouble(OrderItem::getTotalPrice)
                    ));

            result.setTopSellingItems(getTopItems(itemFrequency, 10));
            result.setTopRevenueItems(getTopItemsByRevenue(itemRevenue, 10));

            // Analyze by category
            Map<String, Long> categoryFrequency = allItems.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getCategory().getDisplayName(),
                            Collectors.summingLong(OrderItem::getQuantity)
                    ));

            Map<String, Double> categoryRevenue = allItems.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getCategory().getDisplayName(),
                            Collectors.summingDouble(OrderItem::getTotalPrice)
                    ));

            result.setCategoryPerformance(categoryFrequency);
            result.setCategoryRevenue(categoryRevenue);

            // Analyze item combinations
            result.setFrequentItemCombinations(analyzeItemCombinations(filteredOrders));

            // Analyze seasonal items
            result.setSeasonalTrends(analyzeSeasonalTrends(filteredOrders));

            // Calculate metrics
            result.setTotalItemsSold(allItems.stream()
                    .mapToLong(OrderItem::getQuantity)
                    .sum());
            result.setTotalMenuRevenue(allItems.stream()
                    .mapToDouble(OrderItem::getTotalPrice)
                    .sum());
            result.setAverageItemsPerOrder(filteredOrders.isEmpty() ? 0 :
                    (double) result.getTotalItemsSold() / filteredOrders.size());

            // Identify underperforming items
            result.setUnderperformingItems(identifyUnderperformingItems(allItems));

            return result;

        } catch (Exception e) {
            throw new AnalysisException("MENU_ANALYSIS", "MenuAnalysisService",
                    "Failed to analyze menu items", e);
        }
    }

    private boolean isWithinDateRange(Order order, LocalDateTime start, LocalDateTime end) {
        return order.getOrderPlaced() != null &&
                !order.getOrderPlaced().isBefore(start) &&
                !order.getOrderPlaced().isAfter(end);
    }

    private List<Map.Entry<String, Long>> getTopItems(Map<String, Long> itemFrequency, int limit) {
        return itemFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<String, Double>> getTopItemsByRevenue(Map<String, Double> itemRevenue, int limit) {
        return itemRevenue.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> analyzeItemCombinations(List<Order> orders) {
        Map<String, Integer> combinations = new HashMap<>();

        for (Order order : orders) {
            List<String> itemIds = order.getItems().stream()
                    .map(OrderItem::getItemId)
                    .sorted()
                    .collect(Collectors.toList());

            // Generate pairs
            for (int i = 0; i < itemIds.size(); i++) {
                for (int j = i + 1; j < itemIds.size(); j++) {
                    String combo = itemIds.get(i) + " & " + itemIds.get(j);
                    combinations.put(combo, combinations.getOrDefault(combo, 0) + 1);
                }
            }
        }

        // Return top 10 combinations
        return combinations.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Map<String, Long>> analyzeSeasonalTrends(List<Order> orders) {
        Map<String, Map<String, Long>> seasonalTrends = new HashMap<>();

        for (Order order : orders) {
            String month = order.getOrderPlaced().getMonth().toString();
            Map<String, Long> monthItems = seasonalTrends.getOrDefault(month, new HashMap<>());

            for (OrderItem item : order.getItems()) {
                monthItems.put(item.getItemId(),
                        monthItems.getOrDefault(item.getItemId(), 0L) + item.getQuantity());
            }

            seasonalTrends.put(month, monthItems);
        }

        return seasonalTrends;
    }

    private List<String> identifyUnderperformingItems(List<OrderItem> allItems) {
        Map<String, Long> itemSales = allItems.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getItemId,
                        Collectors.summingLong(OrderItem::getQuantity)
                ));

        long totalSold = itemSales.values().stream().mapToLong(Long::longValue).sum();
        long averageSold = totalSold / (itemSales.isEmpty() ? 1 : itemSales.size());

        return itemSales.entrySet().stream()
                .filter(entry -> entry.getValue() < averageSold * 0.5) // Less than 50% of average
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());
    }

    // Inner class for MenuAnalysisResult
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuAnalysisResult {
        private List<Map.Entry<String, Long>> topSellingItems;
        private List<Map.Entry<String, Double>> topRevenueItems;
        private Map<String, Long> categoryPerformance;
        private Map<String, Double> categoryRevenue;
        private Map<String, Integer> frequentItemCombinations;
        private Map<String, Map<String, Long>> seasonalTrends;
        private long totalItemsSold;
        private double totalMenuRevenue;
        private double averageItemsPerOrder;
        private List<String> underperformingItems;
    }
}