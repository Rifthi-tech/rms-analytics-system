package com.rms.analytics.strategy;

import com.rms.analytics.model.Order;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Implementation - Filter by price range
 */
public class PriceRangeFilter implements FilterStrategy {
    private final double minPrice;
    private final double maxPrice;

    public PriceRangeFilter(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public List<Order> filter(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getTotalPrice() >= minPrice && order.getTotalPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
