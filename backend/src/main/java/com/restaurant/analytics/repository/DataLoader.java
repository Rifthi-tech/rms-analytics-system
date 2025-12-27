package com.restaurant.analytics.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.restaurant.analytics.model.Order;
import org.springframework.stereotype.Repository;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataLoader {
    private static final String CSV_PATH = "../dataset/restaurant_data.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private List<Order> orders;

    public List<Order> loadOrders() {
        if (orders != null) return orders;

        orders = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Order order = new Order();

                order.setOrderId(row[0]);
                order.setCustomerId(row[1]);
                order.setOutletId(row[2]);
                order.setOrderPlaced(parseDateTime(row[3]));
                order.setOrderConfirmed(parseDateTime(row[4]));
                order.setPrepStarted(parseDateTime(row[5]));
                order.setPrepFinished(parseDateTime(row[6]));
                order.setServedTime(parseDateTime(row[7]));
                order.setStatus(row[8]);
                order.setNumItems(parseInteger(row[9]));
                order.setTotalPriceLkr(parseDouble(row[10]));
                order.setPaymentMethod(row[11]);
                order.setItemId(row[12]);
                order.setQuantity(parseInteger(row[13]));
                order.setPriceX(parseDouble(row[14]));
                order.setNameX(row[15]);
                order.setContactNo(row[16]);
                order.setGender(row[17]);
                order.setAge(parseInteger(row[18]));
                order.setJoinDate(parseDateTime(row[19]));
                order.setLoyaltyGroup(row[20]);
                order.setEstimatedTotalSpentLkr(parseDouble(row[21]));
                order.setNameY(row[22]);
                order.setBorough(row[23]);
                order.setCapacity(parseInteger(row[24]));
                order.setOpened(parseDateTime(row[25]));
                order.setName(row[26]);
                order.setCategory(row[27]);
                order.setPriceY(parseDouble(row[28]));
                order.setIsVegetarian(parseBoolean(row[29]));
                order.setSpiceLevel(parseInteger(row[30]));

                orders.add(order);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return value != null && !value.isEmpty() ? LocalDateTime.parse(value, FORMATTER) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            return value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        return value != null && (value.equalsIgnoreCase("true") || value.equals("1"));
    }
}