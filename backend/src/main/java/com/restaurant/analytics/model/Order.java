package com.restaurant.analytics.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private String orderId;
    private String customerId;
    private String outletId;
    private LocalDateTime orderPlaced;
    private LocalDateTime orderConfirmed;
    private LocalDateTime prepStarted;
    private LocalDateTime prepFinished;
    private LocalDateTime servedTime;
    private String status;
    private Integer numItems;
    private Double totalPriceLkr;
    private String paymentMethod;
    private String itemId;
    private Integer quantity;
    private Double priceX;
    private String nameX;
    private String contactNo;
    private String gender;
    private Integer age;
    private LocalDateTime joinDate;
    private String loyaltyGroup;
    private Double estimatedTotalSpentLkr;
    private String nameY;
    private String borough;
    private Integer capacity;
    private LocalDateTime opened;
    private String name;
    private String category;
    private Double priceY;
    private Boolean isVegetarian;
    private Integer spiceLevel;
}