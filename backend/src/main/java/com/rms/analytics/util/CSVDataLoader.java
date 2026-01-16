package com.rms.analytics.util;

import com.rms.analytics.model.Order;
import com.rms.analytics.model.OrderItem;
import com.rms.analytics.model.Customer;
import com.rms.analytics.model.Outlet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * CSV Data Loader - Chunk-based processing for large files
 * Implements chunk-based processing for 500MB files
 */
public class CSVDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(CSVDataLoader.class);
    private static final int CHUNK_SIZE = 10000;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Load CSV in chunks and process with callback
     */
    public static void loadChunkedData(Path csvPath, ChunkProcessor processor) throws IOException {
        try (Reader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<CSVRecord> chunk = new ArrayList<>();
            long totalRecords = 0;
            long startTime = System.currentTimeMillis();

            for (CSVRecord record : csvParser) {
                chunk.add(record);
                if (chunk.size() >= CHUNK_SIZE) {
                    processor.processChunk(chunk);
                    totalRecords += chunk.size();
                    EventManager.getInstance().notifyChunkProcessed((int) totalRecords, 0);
                    chunk.clear();
                }
            }

            // Process remaining records
            if (!chunk.isEmpty()) {
                processor.processChunk(chunk);
                totalRecords += chunk.size();
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            EventManager.getInstance().notifyProcessingComplete(elapsedTime);
            logger.info("Loaded {} records in {} ms", totalRecords, elapsedTime);
        }
    }

    /**
     * Parse a single CSV record into Order object
     */
    public static Order parseOrder(CSVRecord record) {
        try {
            Order order = new Order();
            order.setOrderId(record.get("order_id"));
            order.setCustomerId(record.get("customer_id"));
            order.setOutletId(record.get("outlet_id"));
            order.setOrderPlaced(parseDateTime(record.get("order_placed")));
            order.setOrderConfirmed(parseDateTime(record.get("order_confirmed")));
            order.setPrepStarted(parseDateTime(record.get("prep_started")));
            order.setPrepFinished(parseDateTime(record.get("prep_finished")));
            order.setServedTime(parseDateTime(record.get("served_time")));
            order.setStatus(record.get("status"));
            order.setNumItems(Integer.parseInt(record.get("num_items")));
            order.setTotalPrice(Double.parseDouble(record.get("total_price_lkr")));
            order.setPaymentMethod(record.get("payment_method"));
            return order;
        } catch (Exception e) {
            DeadLetterQueue.getInstance().addDeadLetter(
                record.toString(), 
                "Order parsing failed: " + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Parse a single CSV record into Customer object
     */
    public static Customer parseCustomer(CSVRecord record) {
        try {
            Customer customer = new Customer();
            customer.setCustomerId(record.get("customer_id"));
            customer.setContactNo(record.get("contact_no"));
            customer.setGender(record.get("gender"));
            customer.setAge(Integer.parseInt(record.get("age")));
            customer.setJoinDate(LocalDate.parse(record.get("join_date"), DATE_FORMATTER));
            customer.setLoyaltyGroup(record.get("loyalty_group"));
            customer.setEstimatedTotalSpent(Double.parseDouble(record.get("estimated_total_spent_lkr")));
            return customer;
        } catch (Exception e) {
            DeadLetterQueue.getInstance().addDeadLetter(
                record.toString(), 
                "Customer parsing failed: " + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Parse a single CSV record into OrderItem object
     */
    public static OrderItem parseOrderItem(CSVRecord record) {
        try {
            OrderItem item = new OrderItem();
            item.setItemId(record.get("item_id"));
            item.setItemName(record.get("name"));
            item.setCategory(record.get("category"));
            item.setPrice(Double.parseDouble(record.get("price_lkr_y")));
            item.setQuantity(Integer.parseInt(record.get("quantity")));
            item.setVegetarian(Boolean.parseBoolean(record.get("is_vegetarian")));
            item.setSpiceLevel(record.get("spice_level"));
            return item;
        } catch (Exception e) {
            DeadLetterQueue.getInstance().addDeadLetter(
                record.toString(), 
                "OrderItem parsing failed: " + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Parse a single CSV record into Outlet object
     */
    public static Outlet parseOutlet(CSVRecord record) {
        try {
            Outlet outlet = new Outlet();
            outlet.setOutletId(record.get("outlet_id"));
            outlet.setName(record.get("name_y"));
            outlet.setBorough(record.get("borough"));
            outlet.setCapacity(Integer.parseInt(record.get("capacity")));
            outlet.setOpened(LocalDate.parse(record.get("opened"), DATE_FORMATTER));
            return outlet;
        } catch (Exception e) {
            DeadLetterQueue.getInstance().addDeadLetter(
                record.toString(), 
                "Outlet parsing failed: " + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Parse datetime string
     */
    private static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
        } catch (Exception e) {
            logger.warn("Failed to parse datetime: {}", dateTimeStr);
            return null;
        }
    }

    /**
     * Functional interface for chunk processing
     */
    public interface ChunkProcessor {
        void processChunk(List<CSVRecord> chunk);
    }
}
