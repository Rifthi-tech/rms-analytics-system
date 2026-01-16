package com.rms.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Order entity representing a restaurant order
 * Following Single Responsibility Principle - handles order data only
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private int numItems;
    private double totalPrice;
    private String paymentMethod;
}
