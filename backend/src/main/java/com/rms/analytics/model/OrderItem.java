package com.rms.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderItem entity representing individual items in an order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String itemId;
    private String itemName;
    private String category;
    private double price;
    private int quantity;
    private boolean isVegetarian;
    private String spiceLevel;
}
