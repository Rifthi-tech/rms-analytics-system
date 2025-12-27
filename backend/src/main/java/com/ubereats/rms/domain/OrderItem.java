package com.ubereats.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String itemId;
    private String name;
    private Category category;
    private int quantity;
    private double priceLkr;
    private boolean isVegetarian;
    private String spiceLevel;

    public double getTotalPrice() {
        return quantity * priceLkr;
    }

    public boolean isSpicy() {
        return spiceLevel != null &&
                (spiceLevel.equalsIgnoreCase("high") ||
                        spiceLevel.equalsIgnoreCase("medium"));
    }
}