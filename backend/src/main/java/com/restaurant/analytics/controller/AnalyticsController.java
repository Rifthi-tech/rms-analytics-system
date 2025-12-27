package com.restaurant.analytics.controller;

import com.restaurant.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/peak-dining")
    public Map<String, Object> getPeakDining() {
        return analyticsService.getPeakDiningAnalysis();
    }

    @GetMapping("/customer-segment")
    public Map<String, Object> getCustomerSegment() {
        return analyticsService.getCustomerSegmentation();
    }

    @GetMapping("/seasonal")
    public Map<String, Object> getSeasonal() {
        return analyticsService.getSeasonalBehavior();
    }

    @GetMapping("/menu-items")
    public Map<String, Object> getMenuItems() {
        return analyticsService.getMenuItemAnalysis();
    }

    @GetMapping("/revenue")
    public Map<String, Object> getRevenue() {
        return analyticsService.getRevenueAnalysis();
    }

    @GetMapping("/branch-performance")
    public Map<String, Object> getBranchPerformance() {
        return analyticsService.getBranchPerformance();
    }

    @GetMapping("/anomaly")
    public Map<String, Object> getAnomalies() {
        return analyticsService.getAnomalyDetection();
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}