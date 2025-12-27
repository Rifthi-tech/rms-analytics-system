package com.ubereats.rms.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ubereats.rms.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVDataSource implements DataSourceFactory.DataSource {

    @Value("${data.chunk-size:10000}")
    private int chunkSize;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader("data/sample_orders.csv"))) {
            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    Order order = parseOrder(nextLine);
                    orders.add(order);

                    // Implement chunk-based processing
                    if (orders.size() % chunkSize == 0) {
                        // Process chunk
                        System.out.println("Processed " + orders.size() + " records");
                    }

                } catch (Exception e) {
                    DeadLetterQueue.getInstance().addRecord(nextLine, e.getMessage());
                }
            }

        } catch (IOException | CsvValidationException e) {
            throw new DataIngestionException("Failed to load CSV data", e);
        }

        return orders;
    }

    private Order parseOrder(String[] record) {
        Order order = new Order();
        order.setOrderId(record[0]);
        order.setCustomerId(record[1]);
        order.setOutletId(record[2]);
        order.setOrderPlaced(parseDateTime(record[3]));
        order.setOrderConfirmed(parseDateTime(record[4]));
        order.setPrepStarted(parseDateTime(record[5]));
        order.setPrepFinished(parseDateTime(record[6]));
        order.setServedTime(parseDateTime(record[7]));
        order.setStatus(OrderStatus.valueOf(record[8].toUpperCase()));
        order.setNumItems(Integer.parseInt(record[9]));
        order.setTotalPriceLkr(Double.parseDouble(record[10]));
        order.setPaymentMethod(record[11]);

        // Parse order items
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId(record[12]);
        item.setQuantity(Integer.parseInt(record[13]));
        item.setPriceLkr(Double.parseDouble(record[14]));
        item.setName(record[15]);
        items.add(item);

        order.setItems(items);

        return order;
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    @Override
    public List<Customer> loadCustomers() {
        // Implementation for loading customers
        return new ArrayList<>();
    }

    @Override
    public List<MenuItem> loadMenuItems() {
        // Implementation for loading menu items
        return new ArrayList<>();
    }

    @Override
    public List<Outlet> loadOutlets() {
        // Implementation for loading outlets
        return new ArrayList<>();
    }
}