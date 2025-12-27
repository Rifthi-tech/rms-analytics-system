package com.ubereats.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

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
    private OrderStatus status;
    private int numItems;
    private double totalPriceLkr;
    private String paymentMethod;
    private List<OrderItem> items;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OrderItem {
    private String itemId;
    private int quantity;
    private double priceLkr;
    private String name;
}