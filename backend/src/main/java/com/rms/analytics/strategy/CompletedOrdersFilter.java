package com.rms.analytics.strategy;

import com.rms.analytics.model.Order;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Implementation - Filter completed orders
 */
public class CompletedOrdersFilter implements FilterStrategy {
    @Override
    public List<Order> filter(List<Order> orders) {
        return orders.stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());
    }
}
