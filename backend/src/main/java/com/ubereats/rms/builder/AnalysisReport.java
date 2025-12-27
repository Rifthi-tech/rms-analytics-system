package com.ubereats.rms.builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisReport {
    private String reportId;
    private String reportType;
    private LocalDateTime generatedAt;
    private LocalDateTime analysisPeriodStart;
    private LocalDateTime analysisPeriodEnd;
    private String outletId;
    private Map<String, Object> summary;
    private List<Map<String, Object>> detailedFindings;
    private List<String> recommendations;
    private Map<String, String> metadata;

    public String toJson() {
        // In real implementation, use Jackson or Gson
        return "{\"reportId\":\"" + reportId + "\",\"type\":\"" + reportType + "\"}";
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Report ID,Report Type,Generated At,Analysis Period Start,Analysis Period End,Outlet ID\n");
        sb.append(reportId).append(",")
                .append(reportType).append(",")
                .append(generatedAt).append(",")
                .append(analysisPeriodStart).append(",")
                .append(analysisPeriodEnd).append(",")
                .append(outletId != null ? outletId : "").append("\n");

        if (summary != null) {
            sb.append("\nSummary\n");
            summary.forEach((key, value) -> {
                sb.append(key).append(",").append(value).append("\n");
            });
        }

        return sb.toString();
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "reportId", reportId,
                "reportType", reportType,
                "generatedAt", generatedAt,
                "analysisPeriodStart", analysisPeriodStart,
                "analysisPeriodEnd", analysisPeriodEnd,
                "outletId", outletId,
                "summary", summary,
                "detailedFindings", detailedFindings,
                "recommendations", recommendations,
                "metadata", metadata
        );
    }
}