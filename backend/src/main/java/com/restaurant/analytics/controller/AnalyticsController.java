package com.ubereats.rms.controller;

import com.ubereats.rms.dto.*;
import com.ubereats.rms.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final PeakDiningService peakDiningService;

    public AnalyticsController(PeakDiningService peakDiningService) {
        this.peakDiningService = peakDiningService;
    }

    @PostMapping("/peak-dining")
    public ResponseEntity<AnalysisResponse<PeakDiningResult>> analyzePeakDining(@RequestBody AnalysisRequest request) {
        long startTime = System.currentTimeMillis();

        PeakDiningResult result = peakDiningService.analyzePeakHours(
                request.getStartDate(),
                request.getEndDate(),
                request.getOutletId()
        );

        AnalysisResponse<PeakDiningResult> response = new AnalysisResponse<>();
        response.setData(result);
        response.setProcessingTime(System.currentTimeMillis() - startTime);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("RMS Analytics Service is running");
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("{\"status\":\"success\",\"message\":\"API is working\"}");
    }
}