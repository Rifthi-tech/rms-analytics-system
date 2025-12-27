package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.domain.OrderStatus;
import com.ubereats.rms.dto.PeakDiningResult;
import com.ubereats.rms.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeakDiningServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PeakDiningService peakDiningService;

    private List<Order> testOrders;

    @BeforeEach
    void setUp() {
        // Create test data
        Order order1 = createTestOrder("ORD001", "OUT001",
                LocalDateTime.of(2024, 1, 15, 12, 30), 1500.0);
        Order order2 = createTestOrder("ORD002", "OUT001",
                LocalDateTime.of(2024, 1, 15, 18, 45), 2000.0);
        Order order3 = createTestOrder("ORD003", "OUT001",
                LocalDateTime.of(2024, 1, 15, 18, 15), 1800.0);
        Order order4 = createTestOrder("ORD004", "OUT002",
                LocalDateTime.of(2024, 1, 16, 19, 30), 2500.0);

        testOrders = Arrays.asList(order1, order2, order3, order4);
    }

    @Test
    void testAnalyzePeakHours_withValidData() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);
        String outletId = "OUT001";

        when(orderRepository.findAll()).thenReturn(testOrders);

        // When
        PeakDiningResult result = peakDiningService.analyzePeakHours(startDate, endDate, outletId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getTotalOrders()); // Only orders from OUT001
        assertEquals(5300.0, result.getTotalRevenue(), 0.01); // 1500 + 2000 + 1800

        // Check hour distribution
        assertNotNull(result.getOrdersByHour());
        assertEquals(2, result.getOrdersByHour().get(18)); // Two orders at 18:00 hour

        // Check day distribution
        assertNotNull(result.getOrdersByDay());
        assertTrue(result.getOrdersByDay().containsKey("MONDAY"));
    }

    @Test
    void testAnalyzePeakHours_withAllOutlets() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        when(orderRepository.findAll()).thenReturn(testOrders);

        // When
        PeakDiningResult result = peakDiningService.analyzePeakHours(startDate, endDate, null);

        // Then
        assertNotNull(result);
        assertEquals(4, result.getTotalOrders()); // All orders
        assertEquals(7800.0, result.getTotalRevenue(), 0.01); // Total of all orders
    }

    @Test
    void testAnalyzePeakHours_withDateRangeFilter() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 16, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 16, 23, 59);

        when(orderRepository.findAll()).thenReturn(testOrders);

        // When
        PeakDiningResult result = peakDiningService.analyzePeakHours(startDate, endDate, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalOrders()); // Only order from Jan 16
        assertEquals(2500.0, result.getTotalRevenue(), 0.01);
    }

    @Test
    void testGetPeakHours() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        when(orderRepository.findAll()).thenReturn(testOrders);

        // When
        List<java.time.LocalTime> peakHours = peakDiningService.getPeakHours(startDate, endDate);

        // Then
        assertNotNull(peakHours);
        assertFalse(peakHours.isEmpty());

        // Peak hour should be 18:00 (2 orders)
        assertEquals(java.time.LocalTime.of(18, 0), peakHours.get(0));
    }

    @Test
    void testAnalyzePeakHours_withNoData() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        // When
        PeakDiningResult result = peakDiningService.analyzePeakHours(startDate, endDate, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalOrders());
        assertEquals(0.0, result.getTotalRevenue(), 0.01);
        assertNotNull(result.getOrdersByHour());
        assertTrue(result.getOrdersByHour().isEmpty());
    }

    @Test
    void testAnalyzePeakHours_withInvalidOutlet() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);
        String invalidOutletId = "INVALID_OUTLET";

        when(orderRepository.findAll()).thenReturn(testOrders);

        // When
        PeakDiningResult result = peakDiningService.analyzePeakHours(startDate, endDate, invalidOutletId);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalOrders()); // No orders for invalid outlet
        assertEquals(0.0, result.getTotalRevenue(), 0.01);
    }

    private Order createTestOrder(String orderId, String outletId, LocalDateTime orderTime, double totalPrice) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOutletId(outletId);
        order.setOrderPlaced(orderTime);
        order.setTotalPriceLkr(totalPrice);
        order.setStatus(OrderStatus.DELIVERED);
        order.setNumItems(2);
        order.setPaymentMethod("CARD");
        return order;
    }
}