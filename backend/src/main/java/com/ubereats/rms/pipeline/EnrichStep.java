package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Customer;
import com.ubereats.rms.domain.MenuItem;
import com.ubereats.rms.domain.Order;
import com.ubereats.rms.domain.OrderItem;
import com.ubereats.rms.domain.Outlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EnrichStep implements PipelineStep {

    private Map<String, Customer> customerCache = new HashMap<>();
    private Map<String, MenuItem> menuItemCache = new HashMap<>();
    private Map<String, Outlet> outletCache = new HashMap<>();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OutletService outletService;

    @Override
    public String getName() {
        return "EnrichStep";
    }

    @Override
    public List<Order> process(List<Order> input) {
        return input.stream()
                .map(this::enrichOrder)
                .collect(Collectors.toList());
    }

    private Order enrichOrder(Order order) {
        // Enrich with customer data
        Customer customer = getCustomer(order.getCustomerId());
        if (customer != null) {
            order.setCustomerName(customer.getName());
            order.setCustomerGender(customer.getGender());
            order.setCustomerAge(customer.getAge());
            order.setCustomerLoyaltyTier(customer.getLoyaltyGroup());
            order.setCustomerJoinDate(customer.getJoinDate());
        }

        // Enrich with outlet data
        Outlet outlet = getOutlet(order.getOutletId());
        if (outlet != null) {
            order.setOutletName(outlet.getName());
            order.setOutletBorough(outlet.getBorough());
            order.setOutletCapacity(outlet.getCapacity());
        }

        // Enrich order items with menu data
        List<OrderItem> enrichedItems = order.getItems().stream()
                .map(this::enrichOrderItem)
                .collect(Collectors.toList());
        order.setItems(enrichedItems);

        // Calculate derived fields
        calculateDerivedFields(order);

        return order;
    }

    private OrderItem enrichOrderItem(OrderItem item) {
        MenuItem menuItem = getMenuItem(item.getItemId());
        if (menuItem != null) {
            item.setName(menuItem.getName());
            item.setCategory(menuItem.getCategory());
            item.setVegetarian(menuItem.isVegetarian());
            item.setSpiceLevel(menuItem.getSpiceLevel());
        }
        return item;
    }

    private void calculateDerivedFields(Order order) {
        // Calculate preparation time
        if (order.getPrepStarted() != null && order.getPrepFinished() != null) {
            long prepMinutes = Duration.between(order.getPrepStarted(), order.getPrepFinished()).toMinutes();
            order.setPreparationTimeMinutes(prepMinutes);
        }

        // Calculate wait time (order placed to served)
        if (order.getOrderPlaced() != null && order.getServedTime() != null) {
            long waitMinutes = Duration.between(order.getOrderPlaced(), order.getServedTime()).toMinutes();
            order.setTotalWaitTimeMinutes(waitMinutes);
        }

        // Calculate time to confirmation
        if (order.getOrderPlaced() != null && order.getOrderConfirmed() != null) {
            long confirmSeconds = Duration.between(order.getOrderPlaced(), order.getOrderConfirmed()).toSeconds();
            order.setConfirmationTimeSeconds(confirmSeconds);
        }

        // Determine if peak hour (6-9 PM)
        int orderHour = order.getOrderPlaced().getHour();
        order.setPeakHour(orderHour >= 18 && orderHour <= 21);

        // Determine day of week
        order.setDayOfWeek(order.getOrderPlaced().getDayOfWeek());

        // Determine if weekend
        String day = order.getDayOfWeek().toString();
        order.setWeekend(day.equals("SATURDAY") || day.equals("SUNDAY"));

        // Calculate items total vs order total (for validation)
        double itemsTotal = order.getItems().stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        order.setItemsTotal(itemsTotal);
        order.setTotalDiscrepancy(Math.abs(order.getTotalPriceLkr() - itemsTotal));
    }

    private Customer getCustomer(String customerId) {
        if (!customerCache.containsKey(customerId)) {
            customerCache.put(customerId, customerService.findCustomerById(customerId));
        }
        return customerCache.get(customerId);
    }

    private MenuItem getMenuItem(String itemId) {
        if (!menuItemCache.containsKey(itemId)) {
            menuItemCache.put(itemId, menuService.findMenuItemById(itemId));
        }
        return menuItemCache.get(itemId);
    }

    private Outlet getOutlet(String outletId) {
        if (!outletCache.containsKey(outletId)) {
            outletCache.put(outletId, outletService.findOutletById(outletId));
        }
        return outletCache.get(outletId);
    }

    // Mock service classes (should be injected in real implementation)
    @Component
    private static class CustomerService {
        public Customer findCustomerById(String customerId) {
            // In real implementation, fetch from repository
            return null;
        }
    }

    @Component
    private static class MenuService {
        public MenuItem findMenuItemById(String itemId) {
            // In real implementation, fetch from repository
            return null;
        }
    }

    @Component
    private static class OutletService {
        public Outlet findOutletById(String outletId) {
            // In real implementation, fetch from repository
            return null;
        }
    }
}