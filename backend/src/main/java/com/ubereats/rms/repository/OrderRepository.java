package com.ubereats.rms.repository;

import com.ubereats.rms.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {
    List<Order> findAll();
    List<Order> findByOutletId(String outletId);
    List<Order> findByDateRange(LocalDateTime start, LocalDateTime end);
    Optional<Order> findById(String orderId);
    void saveAll(List<Order> orders);
    long count();
}