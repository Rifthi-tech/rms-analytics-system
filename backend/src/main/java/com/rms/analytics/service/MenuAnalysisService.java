package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Menu Analysis
 * Requirement 4: Popular menu items and order flow analysis
 */
public class MenuAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(MenuAnalysisService.class);

    /**
     * Analyze top-selling menu items from orders
     * Note: Item information is embedded in order records
     */
    public Map<String, MenuItemStats> analyzeTopMenuItems(List<Order> orders) {
        Map<String, MenuItemStats> itemStats = new HashMap<>();
        
        // Group by item name (extracted from orders)
        Map<String, Integer> itemCounts = new HashMap<>();
        
        for (Order order : orders) {
            if (order.getOrderId() != null) {
                itemCounts.put(order.getOrderId(), itemCounts.getOrDefault(order.getOrderId(), 0) + 1);
            }
        }

        // Create statistics
        itemCounts.forEach((itemName, count) -> {
            itemStats.put(itemName, new MenuItemStats(itemName, count, 0.0));
        });

        return itemStats;
    }

    /**
     * Get top N menu items
     */
    public List<MenuItemStats> getTopNMenuItems(List<Order> orders, int n) {
        return analyzeTopMenuItems(orders).values().stream()
                .sorted(Comparator.comparingInt(item -> -item.orderCount))
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Analyze item combinations (co-occurrence)
     */
    public Map<String, Integer> analyzeItemCombinations(List<Order> orders) {
        Map<String, Integer> combinations = new HashMap<>();
        
        // Group orders by order ID to get items in same order
        Map<String, List<Order>> orderItems = orders.stream()
                .collect(Collectors.groupingBy(Order::getOrderId));

        // Find common item pairs
        for (List<Order> items : orderItems.values()) {
            if (items.size() > 1) {
                for (int i = 0; i < items.size(); i++) {
                    for (int j = i + 1; j < items.size(); j++) {
                        String combo = createComboKey(items.get(i), items.get(j));
                        combinations.put(combo, combinations.getOrDefault(combo, 0) + 1);
                    }
                }
            }
        }

        return combinations;
    }

    private String createComboKey(Order item1, Order item2) {
        String id1 = item1.getOrderId();
        String id2 = item2.getOrderId();
        return id1.compareTo(id2) < 0 ? id1 + " + " + id2 : id2 + " + " + id1;
    }

    /**
     * Analyze orders by category
     */
    public Map<String, Integer> analyzeByCategory(List<Order> orders) {
        Map<String, Integer> categories = new HashMap<>();
        // Categories would be extracted from order item data
        // For demo purposes, categorizing by outlet
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getOutletId,
                        Collectors.summingInt(o -> 1)
                ));
    }

    // DTO for response
    public static class MenuItemStats {
        public String itemName;
        public int orderCount;
        public double avgRating;

        public MenuItemStats(String itemName, int orderCount, double avgRating) {
            this.itemName = itemName;
            this.orderCount = orderCount;
            this.avgRating = avgRating;
        }
    }
}
