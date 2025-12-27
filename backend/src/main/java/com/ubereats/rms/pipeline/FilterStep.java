package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class FilterStep implements PipelineStep {

    private Predicate<Order> filterCondition;

    public FilterStep() {
        this.filterCondition = order -> true; // Default: no filtering
    }

    public FilterStep(Predicate<Order> filterCondition) {
        this.filterCondition = filterCondition;
    }

    @Override
    public String getName() {
        return "FilterStep";
    }

    @Override
    public List<Order> process(List<Order> input) {
        return input.stream()
                .filter(filterCondition)
                .collect(Collectors.toList());
    }

    // Factory methods for common filters
    public static FilterStep byDateRange(LocalDateTime start, LocalDateTime end) {
        return new FilterStep(order ->
                order.getOrderPlaced() != null &&
                        !order.getOrderPlaced().isBefore(start) &&
                        !order.getOrderPlaced().isAfter(end));
    }

    public static FilterStep byOutlet(String outletId) {
        return new FilterStep(order -> outletId.equals(order.getOutletId()));
    }

    public static FilterStep byStatus(OrderStatus status) {
        return new FilterStep(order -> status.equals(order.getStatus()));
    }
}