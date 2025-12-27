package com.ubereats.rms.builder;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.dto.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AnalysisReportBuilder {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AnalysisReport buildPeakDiningReport(PeakDiningResult result,
                                                LocalDateTime startDate,
                                                LocalDateTime endDate,
                                                String outletId) {
        AnalysisReport report = new AnalysisReport();
        report.setReportId(UUID.randomUUID().toString());
        report.setReportType("PEAK_DINING_ANALYSIS");
        report.setGeneratedAt(LocalDateTime.now());
        report.setAnalysisPeriodStart(startDate);
        report.setAnalysisPeriodEnd(endDate);
        report.setOutletId(outletId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Total Orders", result.getTotalOrders());
        summary.put("Total Revenue", String.format("LKR %,.2f", result.getTotalRevenue()));
        summary.put("Analysis Period", String.format("%s to %s",
                startDate.format(dateFormatter),
                endDate.format(dateFormatter)));

        if (outletId != null && !outletId.isEmpty()) {
            summary.put("Outlet", outletId);
        } else {
            summary.put("Scope", "All Outlets");
        }

        report.setSummary(summary);

        // Add detailed findings
        List<Map<String, Object>> findings = new ArrayList<>();

        // Peak hours analysis
        if (result.getOrdersByHour() != null && !result.getOrdersByHour().isEmpty()) {
            Map<Integer, Long> sortedHours = result.getOrdersByHour().entrySet().stream()
                    .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                    .limit(5)
                    .collect(LinkedHashMap::new,
                            (m, e) -> m.put(e.getKey(), e.getValue()),
                            LinkedHashMap::putAll);

            Map<String, Object> peakHours = new LinkedHashMap<>();
            peakHours.put("category", "Top 5 Peak Hours");
            peakHours.put("data", sortedHours);
            findings.add(peakHours);
        }

        // Peak days analysis
        if (result.getOrdersByDay() != null && !result.getOrdersByDay().isEmpty()) {
            Map<String, Object> peakDays = new LinkedHashMap<>();
            peakDays.put("category", "Orders by Day of Week");
            peakDays.put("data", result.getOrdersByDay());
            findings.add(peakDays);
        }

        report.setDetailedFindings(findings);

        // Generate recommendations
        List<String> recommendations = generatePeakDiningRecommendations(result);
        report.setRecommendations(recommendations);

        // Set metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("reportVersion", "1.0");
        metadata.put("dataSource", "CSV");
        metadata.put("analysisMethod", "Statistical Analysis");
        report.setMetadata(metadata);

        return report;
    }

    public AnalysisReport buildCustomerSegmentationReport(SegmentationResult result,
                                                          LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        AnalysisReport report = new AnalysisReport();
        report.setReportId(UUID.randomUUID().toString());
        report.setReportType("CUSTOMER_SEGMENTATION_ANALYSIS");
        report.setGeneratedAt(LocalDateTime.now());
        report.setAnalysisPeriodStart(startDate);
        report.setAnalysisPeriodEnd(endDate);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Total Customers", result.getTotalCustomers());
        summary.put("Total Revenue", String.format("LKR %,.2f", result.getTotalRevenue()));
        summary.put("Average Customer Value", String.format("LKR %,.2f", result.getAverageCustomerValue()));
        summary.put("Customer Retention Rate", String.format("%.1f%%", result.getCustomerRetentionRate()));

        if (result.getTopPerformingSegment() != null) {
            summary.put("Top Performing Segment", result.getTopPerformingSegment());
        }

        report.setSummary(summary);

        // Add detailed findings
        List<Map<String, Object>> findings = new ArrayList<>();

        // Loyalty tier distribution
        if (result.getCustomerCountByTier() != null && !result.getCustomerCountByTier().isEmpty()) {
            Map<String, Object> loyaltyTiers = new LinkedHashMap<>();
            loyaltyTiers.put("category", "Customer Distribution by Loyalty Tier");
            loyaltyTiers.put("data", result.getCustomerCountByTier());
            findings.add(loyaltyTiers);
        }

        // Demographic analysis
        if (result.getCustomerCountByAgeGroup() != null && !result.getCustomerCountByAgeGroup().isEmpty()) {
            Map<String, Object> ageGroups = new LinkedHashMap<>();
            ageGroups.put("category", "Customer Distribution by Age Group");
            ageGroups.put("data", result.getCustomerCountByAgeGroup());
            findings.add(ageGroups);
        }

        report.setDetailedFindings(findings);

        // Generate recommendations
        List<String> recommendations = generateSegmentationRecommendations(result);
        report.setRecommendations(recommendations);

        return report;
    }

    public AnalysisReport buildRevenueAnalysisReport(RevenueResult result,
                                                     LocalDateTime startDate,
                                                     LocalDateTime endDate,
                                                     String outletId) {
        AnalysisReport report = new AnalysisReport();
        report.setReportId(UUID.randomUUID().toString());
        report.setReportType("REVENUE_ANALYSIS");
        report.setGeneratedAt(LocalDateTime.now());
        report.setAnalysisPeriodStart(startDate);
        report.setAnalysisPeriodEnd(endDate);
        report.setOutletId(outletId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("Total Revenue", String.format("LKR %,.2f", result.getTotalRevenue()));
        summary.put("Total Orders", String.format("%,.0f", result.getTotalOrders()));
        summary.put("Average Order Value", String.format("LKR %,.2f", result.getAverageOrderValue()));
        summary.put("Net Revenue", String.format("LKR %,.2f", result.getNetRevenue()));
        summary.put("Reconciliation Rate", String.format("%.1f%%", result.getReconciliationRate()));
        summary.put("Revenue Growth Rate", String.format("%.1f%%", result.getRevenueGrowthRate()));

        if (result.getPeakRevenueHour() != null) {
            summary.put("Peak Revenue Hour", result.getPeakRevenueHour());
        }
        if (result.getPeakRevenueOutlet() != null) {
            summary.put("Top Performing Outlet", result.getPeakRevenueOutlet());
        }

        report.setSummary(summary);

        // Add detailed findings
        List<Map<String, Object>> findings = new ArrayList<>();

        // Revenue by hour
        if (result.getRevenueByHour() != null && !result.getRevenueByHour().isEmpty()) {
            Map<String, Object> revenueByHour = new LinkedHashMap<>();
            revenueByHour.put("category", "Revenue Distribution by Hour");
            revenueByHour.put("data", result.getRevenueByHour());
            findings.add(revenueByHour);
        }

        // Revenue by payment method
        if (result.getRevenueByPaymentMethod() != null && !result.getRevenueByPaymentMethod().isEmpty()) {
            Map<String, Object> revenueByPayment = new LinkedHashMap<>();
            revenueByPayment.put("category", "Revenue by Payment Method");
            revenueByPayment.put("data", result.getRevenueByPaymentMethod());
            findings.add(revenueByPayment);
        }

        report.setDetailedFindings(findings);

        // Generate recommendations
        List<String> recommendations = generateRevenueRecommendations(result);
        report.setRecommendations(recommendations);

        return report;
    }

    private List<String> generatePeakDiningRecommendations(PeakDiningResult result) {
        List<String> recommendations = new ArrayList<>();

        if (result.getOrdersByHour() == null || result.getOrdersByHour().isEmpty()) {
            return recommendations;
        }

        // Find peak hours
        Optional<Map.Entry<Integer, Long>> peakHour = result.getOrdersByHour().entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (peakHour.isPresent()) {
            recommendations.add(String.format(
                    "Peak dining hour is %d:00 with %d orders. Consider increasing staff during this period.",
                    peakHour.get().getKey(), peakHour.get().getValue()));
        }

        // Check for low-traffic hours
        Optional<Map.Entry<Integer, Long>> slowHour = result.getOrdersByHour().entrySet().stream()
                .min(Map.Entry.comparingByValue());

        if (slowHour.isPresent() && slowHour.get().getValue() < 10) {
            recommendations.add(String.format(
                    "Low traffic hour is %d:00 with only %d orders. Consider promotions during this time.",
                    slowHour.get().getKey(), slowHour.get().getValue()));
        }

        // Weekend vs weekday analysis
        if (result.getOrdersByDay() != null) {
            long weekendOrders = result.getOrdersByDay().entrySet().stream()
                    .filter(e -> e.getKey().equals("SATURDAY") || e.getKey().equals("SUNDAY"))
                    .mapToLong(Map.Entry::getValue)
                    .sum();

            long weekdayOrders = result.getTotalOrders() - weekendOrders;

            if (weekendOrders > weekdayOrders * 1.5) {
                recommendations.add("Weekends account for significantly more orders than weekdays. Consider weekend-specific promotions.");
            }
        }

        return recommendations;
    }

    private List<String> generateSegmentationRecommendations(SegmentationResult result) {
        List<String> recommendations = new ArrayList<>();

        if (result.getCustomerCountByTier() != null && !result.getCustomerCountByTier().isEmpty()) {
            // Analyze loyalty tier distribution
            long bronzeCustomers = result.getCustomerCountByTier().getOrDefault(LoyaltyTier.BRONZE, 0L);
            long totalCustomers = result.getCustomerCountByTier().values().stream()
                    .mapToLong(Long::longValue).sum();

            if (bronzeCustomers > totalCustomers * 0.5) {
                recommendations.add("Over 50% of customers are in Bronze tier. Consider implementing loyalty program enhancements.");
            }
        }

        if (result.getCustomerRetentionRate() < 60) {
            recommendations.add(String.format(
                    "Customer retention rate is low (%.1f%%). Consider implementing retention strategies.",
                    result.getCustomerRetentionRate()));
        }

        if (result.getOverallClv() < 5000) {
            recommendations.add(String.format(
                    "Average Customer Lifetime Value is LKR %,.2f. Consider strategies to increase customer value.",
                    result.getOverallClv()));
        }

        return recommendations;
    }

    private List<String> generateRevenueRecommendations(RevenueResult result) {
        List<String> recommendations = new ArrayList<>();

        if (result.getReconciliationRate() < 95) {
            recommendations.add(String.format(
                    "Revenue reconciliation rate is %.1f%%. Investigate payment discrepancies.",
                    result.getReconciliationRate()));
        }

        if (result.getRevenueGrowthRate() < 10) {
            recommendations.add(String.format(
                    "Revenue growth rate is %.1f%%. Consider growth initiatives.",
                    result.getRevenueGrowthRate()));
        }

        if (result.getCancelledAmount() > result.getTotalRevenue() * 0.05) {
            recommendations.add(String.format(
                    "Cancellation amount (LKR %,.2f) is high. Review cancellation policies.",
                    result.getCancelledAmount()));
        }

        return recommendations;
    }

    public String generateReportAsText(AnalysisReport report) {
        StringBuilder sb = new StringBuilder();

        sb.append("=".repeat(80)).append("\n");
        sb.append("RMS ANALYTICS REPORT\n");
        sb.append("=".repeat(80)).append("\n\n");

        sb.append("Report ID: ").append(report.getReportId()).append("\n");
        sb.append("Report Type: ").append(report.getReportType()).append("\n");
        sb.append("Generated At: ").append(report.getGeneratedAt().format(dateFormatter)).append("\n");
        sb.append("Analysis Period: ").append(report.getAnalysisPeriodStart().format(dateFormatter))
                .append(" to ").append(report.getAnalysisPeriodEnd().format(dateFormatter)).append("\n");

        if (report.getOutletId() != null && !report.getOutletId().isEmpty()) {
            sb.append("Outlet: ").append(report.getOutletId()).append("\n");
        }

        sb.append("\n").append("-".repeat(80)).append("\n");
        sb.append("SUMMARY\n");
        sb.append("-".repeat(80)).append("\n");

        for (Map.Entry<String, Object> entry : report.getSummary().entrySet()) {
            sb.append(String.format("%-30s: %s%n", entry.getKey(), entry.getValue()));
        }

        if (report.getDetailedFindings() != null && !report.getDetailedFindings().isEmpty()) {
            sb.append("\n").append("-".repeat(80)).append("\n");
            sb.append("DETAILED FINDINGS\n");
            sb.append("-".repeat(80)).append("\n");

            for (Map<String, Object> finding : report.getDetailedFindings()) {
                sb.append("\n").append(finding.get("category")).append(":\n");
                Object data = finding.get("data");
                if (data instanceof Map) {
                    ((Map<?, ?>) data).forEach((key, value) -> {
                        sb.append(String.format("  %-20s: %s%n", key, value));
                    });
                }
            }
        }

        if (report.getRecommendations() != null && !report.getRecommendations().isEmpty()) {
            sb.append("\n").append("-".repeat(80)).append("\n");
            sb.append("RECOMMENDATIONS\n");
            sb.append("-".repeat(80)).append("\n");

            for (int i = 0; i < report.getRecommendations().size(); i++) {
                sb.append(String.format("%d. %s%n", i + 1, report.getRecommendations().get(i)));
            }
        }

        sb.append("\n").append("=".repeat(80)).append("\n");
        sb.append("END OF REPORT\n");
        sb.append("=".repeat(80)).append("\n");

        return sb.toString();
    }
}