package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.dto.RevenueResult;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RevenueAnalysisService {

    @Autowired
    private OrderRepository orderRepository;

    public RevenueResult analyzeRevenue(LocalDateTime startDate, LocalDateTime endDate, String outletId) {
        List<Order> orders = orderRepository.findAll();

        List<Order> filteredOrders = orders.stream()
                .filter(order -> order.getOrderPlaced() != null &&
                        !order.getOrderPlaced().isBefore(startDate) &&
                        !order.getOrderPlaced().isAfter(endDate))
                .filter(order -> outletId == null || outletId.isEmpty() ||
                        outletId.equals(order.getOutletId()))
                .collect(Collectors.toList());

        RevenueResult result = new RevenueResult();
        result.setTotalRevenue(filteredOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum());

        result.setTotalOrders(filteredOrders.size());

        result.setAverageOrderValue(result.getTotalRevenue() /
                (filteredOrders.isEmpty() ? 1 : filteredOrders.size()));

        // Revenue by payment method
        Map<String, Double> revenueByPaymentMethod = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        Order::getPaymentMethod,
                        Collectors.summingDouble(Order::getTotalPriceLkr)
                ));
        result.setRevenueByPaymentMethod(revenueByPaymentMethod);

        // Revenue by hour
        Map<Integer, Double> revenueByHour = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getHour(),
                        Collectors.summingDouble(Order::getTotalPriceLkr)
                ));
        result.setRevenueByHour(revenueByHour);

        return result;
    }
}