package com.ubereats.rms.controller;

import com.ubereats.rms.dto.*;
import com.ubereats.rms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AnalyticsController {

    @Autowired
    private PeakDiningService peakDiningService;

    @Autowired
    private CustomerSegmentationService segmentationService;

    @Autowired
    private RevenueAnalysisService revenueAnalysisService;

    @Autowired
    private MenuAnalysisService menuAnalysisService;

    @Autowired
    private SeasonalBehaviorService seasonalBehaviorService;

    @Autowired
    private AnomalyDetectionService anomalyDetectionService;

    @Autowired
    private BranchPerformanceService branchPerformanceService;

    @PostMapping("/peak-dining")
    public ResponseEntity<AnalysisResponse<PeakDiningResult>> analyzePeakDining(
            @RequestBody AnalysisRequest request) {

        PeakDiningResult result = peakDiningService.analyzePeakHours(
                request.getStartDate(),
                request.getEndDate(),
                request.getOutletId()
        );

        AnalysisResponse<PeakDiningResult> response = new AnalysisResponse<>();
        response.setSuccess(true);
        response.setData(result);
        response.setMessage("Peak dining analysis completed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/customer-segmentation")
    public ResponseEntity<AnalysisResponse<SegmentationResult>> analyzeCustomerSegmentation(
            @RequestBody AnalysisRequest request) {

        // In real implementation, load customers from repository
        SegmentationResult result = segmentationService.segmentByLoyalty(null);

        AnalysisResponse<SegmentationResult> response = new AnalysisResponse<>();
        response.setSuccess(true);
        response.setData(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/revenue-analysis")
    public ResponseEntity<AnalysisResponse<RevenueResult>> analyzeRevenue(
            @RequestBody AnalysisRequest request) {

        RevenueResult result = revenueAnalysisService.analyzeRevenue(
                request.getStartDate(),
                request.getEndDate(),
                request.getOutletId()
        );

        AnalysisResponse<RevenueResult> response = new AnalysisResponse<>();
        response.setSuccess(true);
        response.setData(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("RMS Analytics Service is running");
    }
}