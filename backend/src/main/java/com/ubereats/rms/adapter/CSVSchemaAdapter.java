package com.ubereats.rms.adapter;

import com.ubereats.rms.domain.*;
import com.ubereats.rms.exception.DataIngestionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CSVSchemaAdapter {

    private static final List<String> EXPECTED_HEADERS = Arrays.asList(
            "order_id", "customer_id", "outlet_id", "order_placed", "order_confirmed",
            "prep_started", "prep_finished", "served_time", "status", "num_items",
            "total_price_lkr", "payment_method", "item_id", "quantity", "price_lkr_x",
            "name_x", "contact_no", "gender", "age", "join_date", "loyalty_group",
            "estimated_total_spent_lkr", "name_y", "borough", "capacity", "opened",
            "name", "category", "price_lkr_y", "is_vegetarian", "spice_level"
    );

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Schema mapping for different CSV formats
    private final Map<String, Map<String, String>> schemaMappings = new HashMap<>();

    public CSVSchemaAdapter() {
        // Uber Eats standard schema
        Map<String, String> uberEatsSchema = new HashMap<>();
        for (String header : EXPECTED_HEADERS) {
            uberEatsSchema.put(header, header);
        }
        schemaMappings.put("UBER_EATS_STANDARD", uberEatsSchema);

        // Legacy schema mapping
        Map<String, String> legacySchema = new HashMap<>();
        legacySchema.put("order_id", "OrderID");
        legacySchema.put("customer_id", "CustomerID");
        legacySchema.put("outlet_id", "StoreID");
        legacySchema.put("order_placed", "OrderTime");
        legacySchema.put("status", "OrderStatus");
        legacySchema.put("total_price_lkr", "TotalAmount");
        // Add more mappings as needed
        schemaMappings.put("LEGACY_V1", legacySchema);
    }

    public Map<String, Integer> validateAndMapHeaders(String[] headers, String schemaType) {
        Map<String, Integer> headerMap = new HashMap<>();

        if ("UBER_EATS_STANDARD".equals(schemaType)) {
            // Standard schema - exact match required
            if (headers.length != EXPECTED_HEADERS.size()) {
                throw new DataIngestionException(
                        String.format("Expected %d columns, found %d",
                                EXPECTED_HEADERS.size(), headers.length));
            }

            for (int i = 0; i < headers.length; i++) {
                String expected = EXPECTED_HEADERS.get(i);
                String actual = headers[i].trim().toLowerCase();

                if (!expected.equals(actual)) {
                    throw new DataIngestionException(
                            String.format("Header mismatch at column %d: expected '%s', found '%s'",
                                    i + 1, expected, actual));
                }
                headerMap.put(expected, i);
            }
        } else {
            // Map based on schema mapping
            Map<String, String> mapping = schemaMappings.get(schemaType);
            if (mapping == null) {
                throw new DataIngestionException("Unknown schema type: " + schemaType);
            }

            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                for (Map.Entry<String, String> entry : mapping.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(header)) {
                        headerMap.put(entry.getKey(), i);
                        break;
                    }
                }
            }

            // Check if all required fields are present
            List<String> requiredFields = Arrays.asList(
                    "order_id", "customer_id", "outlet_id", "order_placed", "total_price_lkr"
            );

            for (String field : requiredFields) {
                if (!headerMap.containsKey(field)) {
                    throw new DataIngestionException(
                            "Missing required field in CSV: " + field);
                }
            }
        }

        return headerMap;
    }

    public Order adaptToOrder(String[] record, Map<String, Integer> headerMap) {
        try {
            Order order = new Order();

            // Map basic fields
            order.setOrderId(getValue(record, headerMap, "order_id"));
            order.setCustomerId(getValue(record, headerMap, "customer_id"));
            order.setOutletId(getValue(record, headerMap, "outlet_id"));

            // Parse timestamps
            order.setOrderPlaced(parseDateTime(getValue(record, headerMap, "order_placed")));
            order.setOrderConfirmed(parseDateTime(getValue(record, headerMap, "order_confirmed")));
            order.setPrepStarted(parseDateTime(getValue(record, headerMap, "prep_started")));
            order.setPrepFinished(parseDateTime(getValue(record, headerMap, "prep_finished")));
            order.setServedTime(parseDateTime(getValue(record, headerMap, "served_time")));

            // Parse status
            String statusStr = getValue(record, headerMap, "status");
            order.setStatus(OrderStatus.fromString(statusStr));

            // Parse numeric fields
            order.setNumItems(parseInt(getValue(record, headerMap, "num_items")));
            order.setTotalPriceLkr(parseDouble(getValue(record, headerMap, "total_price_lkr")));

            // Payment method
            order.setPaymentMethod(getValue(record, headerMap, "payment_method"));

            // Create order items
            OrderItem item = new OrderItem();
            item.setItemId(getValue(record, headerMap, "item_id"));
            item.setQuantity(parseInt(getValue(record, headerMap, "quantity")));
            item.setPriceLkr(parseDouble(getValue(record, headerMap, "price_lkr_x")));
            item.setName(getValue(record, headerMap, "name_x"));

            // Additional item details if available
            if (headerMap.containsKey("category")) {
                item.setCategory(Category.fromString(getValue(record, headerMap, "category")));
            }
            if (headerMap.containsKey("is_vegetarian")) {
                item.setVegetarian(parseBoolean(getValue(record, headerMap, "is_vegetarian")));
            }
            if (headerMap.containsKey("spice_level")) {
                item.setSpiceLevel(getValue(record, headerMap, "spice_level"));
            }

            order.setItems(Arrays.asList(item));

            return order;

        } catch (Exception e) {
            throw new DataIngestionException(
                    String.format("Failed to adapt CSV record to Order: %s",
                            String.join(",", record)), e);
        }
    }

    public Customer adaptToCustomer(String[] record, Map<String, Integer> headerMap) {
        try {
            Customer customer = new Customer();

            customer.setCustomerId(getValue(record, headerMap, "customer_id"));
            customer.setName(getValue(record, headerMap, "name_x"));
            customer.setContactNo(getValue(record, headerMap, "contact_no"));
            customer.setGender(Gender.fromString(getValue(record, headerMap, "gender")));
            customer.setAge(parseInt(getValue(record, headerMap, "age")));

            String joinDate = getValue(record, headerMap, "join_date");
            if (joinDate != null && !joinDate.isEmpty()) {
                customer.setJoinDate(LocalDateTime.parse(joinDate, formatter).toLocalDate());
            }

            customer.setLoyaltyGroup(LoyaltyTier.fromString(getValue(record, headerMap, "loyalty_group")));

            String spentStr = getValue(record, headerMap, "estimated_total_spent_lkr");
            customer.setEstimatedTotalSpentLkr(parseDouble(spentStr));

            return customer;

        } catch (Exception e) {
            throw new DataIngestionException(
                    String.format("Failed to adapt CSV record to Customer: %s",
                            String.join(",", record)), e);
        }
    }

    public Outlet adaptToOutlet(String[] record, Map<String, Integer> headerMap) {
        try {
            Outlet outlet = new Outlet();

            outlet.setOutletId(getValue(record, headerMap, "outlet_id"));
            outlet.setName(getValue(record, headerMap, "name_y"));
            outlet.setBorough(getValue(record, headerMap, "borough"));
            outlet.setCapacity(parseInt(getValue(record, headerMap, "capacity")));

            String opened = getValue(record, headerMap, "opened");
            if (opened != null && !opened.isEmpty()) {
                outlet.setOpened(LocalDateTime.parse(opened, formatter));
            }

            return outlet;

        } catch (Exception e) {
            throw new DataIngestionException(
                    String.format("Failed to adapt CSV record to Outlet: %s",
                            String.join(",", record)), e);
        }
    }

    private String getValue(String[] record, Map<String, Integer> headerMap, String field) {
        Integer index = headerMap.get(field);
        if (index == null || index >= record.length) {
            return null;
        }
        return record[index].trim();
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            // Try parsing as date only
            try {
                return LocalDateTime.parse(dateTimeStr + " 00:00:00", formatter);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    private int parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private boolean parseBoolean(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        value = value.toLowerCase();
        return value.equals("true") || value.equals("yes") || value.equals("1") || value.equals("y");
    }

    public List<String> getSupportedSchemas() {
        return List.copyOf(schemaMappings.keySet());
    }
}