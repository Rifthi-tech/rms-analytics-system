package com.ubereats.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private String itemId;
    private String name;
    private Category category;
    private double priceLkrY;
    private boolean isVegetarian;
    private String spiceLevel;
}