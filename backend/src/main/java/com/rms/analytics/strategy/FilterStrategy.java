package com.rms.analytics.strategy;

import com.rms.analytics.model.Order;
import java.util.List;

/**
 * Strategy Pattern - Different filtering strategies for orders
 */
public interface FilterStrategy {
    List<Order> filter(List<Order> orders);
}
