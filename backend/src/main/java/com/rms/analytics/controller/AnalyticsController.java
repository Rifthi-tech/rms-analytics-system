package com.rms.analytics.controller;

import com.rms.analytics.model.Order;
import com.rms.analytics.model.Customer;
import com.rms.analytics.model.OrderItem;
import com.rms.analytics.service.AnalyticsServiceFacade;
import com.rms.analytics.util.CSVDataLoader;
import com.rms.analytics.util.DeadLetterQueue;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * REST Controller for Analytics
 * Implements RESTful API for all analytics endpoints
 */
@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyticsController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsServiceFacade analyticsFacade;
    private boolean dataLoaded = false;

    public AnalyticsController() {
        this.analyticsFacade = new AnalyticsServiceFacade();
    }

    /**
     * Load CSV data from file
     */
    @PostMapping("/load-data")
    public ResponseEntity<Map<String, Object>> loadData(@RequestParam String filePath) {
        Map<String, Object> response = new HashMap<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File not found: " + filePath));
            }

            List<Order> orders = new ArrayList<>();
            List<Customer> customers = new ArrayList<>();
            List<OrderItem> items = new ArrayList<>();

            // Load CSV in chunks
            CSVDataLoader.loadChunkedData(Paths.get(filePath), chunk -> {
                for (CSVRecord record : chunk) {
                    Order order = CSVDataLoader.parseOrder(record);
                    Customer customer = CSVDataLoader.parseCustomer(record);
                    OrderItem item = CSVDataLoader.parseOrderItem(record);

                    if (order != null) orders.add(order);
                    if (customer != null) customers.add(customer);
                    if (item != null) items.add(item);
                }
            });

            analyticsFacade.loadData(orders, customers, items, new ArrayList<>());
            dataLoaded = true;

            response.put("success", true);
            response.put("ordersLoaded", orders.size());
            response.put("customersLoaded", customers.size());
            response.put("itemsLoaded", items.size());
            response.put("deadLetterCount", DeadLetterQueue.getInstance().getCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error loading data", e);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Get complete analytics report
     */
    @GetMapping("/report")
    public ResponseEntity<?> getCompleteReport() {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded. Please load data first."));
        }
        try {
            return ResponseEntity.ok(analyticsFacade.generateCompleteReport());
        } catch (Exception e) {
            logger.error("Error generating report", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get peak dining analysis
     */
    @GetMapping("/peak-dining")
    public ResponseEntity<?> getPeakDining() {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded."));
        }
        return ResponseEntity.ok(analyticsFacade.getPeakDiningAnalysis());
    }

    /**
     * Get customer demographics
     */
    @GetMapping("/customer-demographics")
    public ResponseEntity<?> getCustomerDemographics() {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded."));
        }
        return ResponseEntity.ok(analyticsFacade.getCustomerDemographics());
    }

    /**
     * Get revenue analysis
     */
    @GetMapping("/revenue-analysis")
    public ResponseEntity<?> getRevenueAnalysis() {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded."));
        }
        return ResponseEntity.ok(analyticsFacade.getRevenueAnalysis());
    }

    /**
     * Get branch performance ranking
     */
    @GetMapping("/branch-performance")
    public ResponseEntity<?> getBranchPerformance(@RequestParam(defaultValue = "revenue") String metric) {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded."));
        }
        return ResponseEntity.ok(analyticsFacade.getBranchRanking(metric));
    }

    /**
     * Get anomaly detection report
     */
    @GetMapping("/anomalies")
    public ResponseEntity<?> getAnomalies(@RequestParam(defaultValue = "ordercount") String type) {
        if (!dataLoaded) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data not loaded."));
        }
        return ResponseEntity.ok(analyticsFacade.getAnomalyReport(type));
    }

    /**
     * Get dead letter queue records
     */
    @GetMapping("/dead-letters")
    public ResponseEntity<?> getDeadLetters() {
        return ResponseEntity.ok(Map.of(
                "count", DeadLetterQueue.getInstance().getCount(),
                "records", DeadLetterQueue.getInstance().getDeadLetters()
        ));
    }

    /**
     * Get system status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of(
                "dataLoaded", dataLoaded,
                "deadLetterCount", DeadLetterQueue.getInstance().getCount(),
                "timestamp", System.currentTimeMillis()
        ));
    }
}
