package com.ubereats.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String outletId;
    private String analysisType;
    private String filters;
}