package com.ubereats.rms.service;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.dto.PeakDiningResult;
import com.ubereats.rms.pipeline.DefaultPipeline;
import com.ubereats.rms.pipeline.FilterStep;
import com.ubereats.rms.pipeline.AggregateStep;
import com.ubereats.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PeakDiningService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DefaultPipeline pipeline;

    public PeakDiningResult analyzePeakHours(LocalDateTime startDate, LocalDateTime endDate, String outletId) {

        // Build pipeline
        pipeline.addStep(FilterStep.byDateRange(startDate, endDate));

        if (outletId != null && !outletId.isEmpty()) {
            pipeline.addStep(FilterStep.byOutlet(outletId));
        }

        List<Order> filteredOrders = pipeline.execute(orderRepository.findAll());

        // Analyze peak hours
        Map<Integer, Long> ordersByHour = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getHour(),
                        Collectors.counting()
                ));

        // Analyze peak days
        Map<String, Long> ordersByDay = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getDayOfWeek().toString(),
                        Collectors.counting()
                ));

        PeakDiningResult result = new PeakDiningResult();
        result.setOrdersByHour(ordersByHour);
        result.setOrdersByDay(ordersByDay);
        result.setTotalOrders(filteredOrders.size());
        result.setTotalRevenue(filteredOrders.stream()
                .mapToDouble(Order::getTotalPriceLkr)
                .sum());

        return result;
    }

    public List<LocalTime> getPeakHours(LocalDateTime startDate, LocalDateTime endDate) {
        PeakDiningResult result = analyzePeakHours(startDate, endDate, null);

        return result.getOrdersByHour().entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .map(entry -> LocalTime.of(entry.getKey(), 0))
                .collect(Collectors.toList());
    }
}