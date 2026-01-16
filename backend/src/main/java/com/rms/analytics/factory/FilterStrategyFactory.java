package com.rms.analytics.factory;

import com.rms.analytics.strategy.FilterStrategy;
import com.rms.analytics.strategy.CompletedOrdersFilter;
import com.rms.analytics.strategy.PriceRangeFilter;

/**
 * Factory Pattern - Creates filter strategies
 * Decouples filter creation from usage
 */
public class FilterStrategyFactory {
    
    public enum FilterType {
        COMPLETED_ORDERS,
        PRICE_RANGE
    }

    private FilterStrategyFactory() {
        // Private constructor for factory class
    }

    /**
     * Create a filter strategy based on type
     */
    public static FilterStrategy createFilter(FilterType type, Object... params) {
        switch (type) {
            case COMPLETED_ORDERS:
                return new CompletedOrdersFilter();
            case PRICE_RANGE:
                if (params.length >= 2) {
                    return new PriceRangeFilter((double) params[0], (double) params[1]);
                }
                return new PriceRangeFilter(0, Double.MAX_VALUE);
            default:
                throw new IllegalArgumentException("Unknown filter type: " + type);
        }
    }
}
