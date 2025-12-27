package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AggregateStep implements PipelineStep {

    private AggregationType aggregationType;

    public enum AggregationType {
        BY_HOUR, BY_DAY, BY_MONTH, BY_OUTLET, BY_STATUS
    }

    public AggregateStep(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public String getName() {
        return "AggregateStep[" + aggregationType + "]";
    }

    @Override
    public List<Order> process(List<Order> input) {
        // This is a simplified version - in real implementation, you'd create aggregated results
        return input; // Return original for pipeline continuation
    }

    public Map<String, Double> aggregateRevenueByHour(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderPlaced().getHour() + ":00",
                        Collectors.summingDouble(Order::getTotalPriceLkr)
                ));
    }

    public Map<String, Long> aggregateOrdersByOutlet(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getOutletId,
                        Collectors.counting()
                ));
    }
}