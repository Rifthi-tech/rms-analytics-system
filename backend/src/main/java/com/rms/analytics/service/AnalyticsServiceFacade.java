package com.rms.analytics.service;

import com.rms.analytics.model.Order;
import com.rms.analytics.model.Customer;
import com.rms.analytics.model.OrderItem;
import com.rms.analytics.model.Outlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Facade Pattern - Unified interface for all analytics
 * Simplifies complex subsystem interactions for clients
 */
public class AnalyticsServiceFacade {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceFacade.class);

    // Inject analytics services
    private final PeakDiningAnalysisService peakDiningService;
    private final CustomerDemographicsService demographicsService;
    private final RevenueAnalysisService revenueService;
    private final MenuAnalysisService menuService;
    private final AnomalyDetectionService anomalyService;
    private final BranchPerformanceService branchService;

    private List<Order> orders;
    private List<Customer> customers;

    public AnalyticsServiceFacade() {
        this.peakDiningService = new PeakDiningAnalysisService();
        this.demographicsService = new CustomerDemographicsService();
        this.revenueService = new RevenueAnalysisService();
        this.menuService = new MenuAnalysisService();
        this.anomalyService = new AnomalyDetectionService();
        this.branchService = new BranchPerformanceService();

        this.orders = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.items = new ArrayList<>();
        this.outlets = new ArrayList<>();
    }

    /**
     * Load data into the facade
     */
    public void loadData(List<Order> orders, List<Customer> customers, 
                        List<OrderItem> items, List<Outlet> outlets) {
        this.orders = orders;
        this.customers = customers;
        this.items = items;
        this.outlets = outlets;
        logger.info("Data loaded: {} orders, {} customers, {} items, {} outlets",
                orders.size(), customers.size(), items.size(), outlets.size());
    }

    /**
     * Get all analytics reports
     */
    public CompleteAnalyticsReport generateCompleteReport() {
        logger.info("Generating complete analytics report");
        
        CompleteAnalyticsReport report = new CompleteAnalyticsReport();

        // Peak Dining Analysis
        report.peakHourAnalysis = peakDiningService.getPeakHourAnalysis(orders);
        report.branchPeakData = peakDiningService.getBranchPeakSummary(orders);
        report.dailyPeaks = peakDiningService.analyzeDailyPeaks(orders);
        report.monthlyPeaks = peakDiningService.analyzeMonthlyPeaks(orders);

        // Customer Demographics
        report.genderDistribution = demographicsService.analyzeByGender(customers);
        report.ageGroupDistribution = demographicsService.analyzeByAgeGroup(customers);
        report.loyaltySegmentation = demographicsService.getLoyaltyStats(customers);

        // Revenue Analysis
        report.totalRevenue = revenueService.calculateTotalRevenue(orders);
        report.revenueByOutlet = revenueService.getRevenueByOutlet(orders);
        report.revenueByPaymentMethod = revenueService.getRevenueByPaymentMethod(orders);
        report.dailySalesSummary = revenueService.getDailySalesSummary(orders);
        report.totalOrderCount = revenueService.countTotalOrders(orders);
        report.ordersByOutlet = revenueService.countOrdersByOutlet(orders);

        // Menu Analysis
        report.topMenuItems = menuService.getTopNMenuItems(orders, 10);
        report.itemCombinations = menuService.analyzeItemCombinations(orders);
        report.categoriesAnalysis = menuService.analyzeByCategory(orders);

        // Anomaly Detection
        report.orderCountAnomalies = anomalyService.detectOrderCountAnomalies(orders);
        report.cancellationAnomalies = anomalyService.detectCancellationAnomalies(orders);
        report.revenueAnomalies = anomalyService.detectRevenueAnomalies(orders);

        // Branch Performance
        report.branchPerformance = branchService.analyzeBranchPerformance(orders);
        report.branchRanking = branchService.rankBranchesByPerformance(orders, "revenue");
        report.underperformingBranches = branchService.identifyUnderperformers(orders);

        return report;
    }

    /**
     * Get peak dining analysis only
     */
    public PeakDiningAnalysisService.PeakHourAnalysis getPeakDiningAnalysis() {
        return peakDiningService.getPeakHourAnalysis(orders);
    }

    /**
     * Get customer demographics only
     */
    public CustomerDemographicsService.LoyaltySegmentationStats getCustomerDemographics() {
        return demographicsService.getLoyaltyStats(customers);
    }

    /**
     * Get revenue analysis only
     */
    public RevenueAnalysisService.DailySalesSummary getRevenueAnalysis() {
        return revenueService.getDailySalesSummary(orders);
    }

    /**
     * Get branch performance ranking
     */
    public List<BranchPerformanceService.BranchPerformance> getBranchRanking(String metric) {
        return branchService.rankBranchesByPerformance(orders, metric);
    }

    /**
     * Get anomaly report
     */
    public AnomalyDetectionService.AnomalyReport getAnomalyReport(String type) {
        switch (type.toLowerCase()) {
            case "ordercount":
                return anomalyService.detectOrderCountAnomalies(orders);
            case "cancellation":
                return anomalyService.detectCancellationAnomalies(orders);
            case "revenue":
                return anomalyService.detectRevenueAnomalies(orders);
            default:
                return anomalyService.detectOrderCountAnomalies(orders);
        }
    }

    // Comprehensive DTO for all reports
    public static class CompleteAnalyticsReport {
        public PeakDiningAnalysisService.PeakHourAnalysis peakHourAnalysis;
        public Map<String, PeakDiningAnalysisService.BranchPeakData> branchPeakData;
        public Map<Integer, Integer> dailyPeaks;
        public Map<Integer, Integer> monthlyPeaks;

        public Map<String, Integer> genderDistribution;
        public Map<String, Integer> ageGroupDistribution;
        public CustomerDemographicsService.LoyaltySegmentationStats loyaltySegmentation;

        public double totalRevenue;
        public Map<String, Double> revenueByOutlet;
        public Map<String, Double> revenueByPaymentMethod;
        public RevenueAnalysisService.DailySalesSummary dailySalesSummary;
        public long totalOrderCount;
        public Map<String, Long> ordersByOutlet;

        public List<MenuAnalysisService.MenuItemStats> topMenuItems;
        public Map<String, Integer> itemCombinations;
        public Map<String, Integer> categoriesAnalysis;

        public AnomalyDetectionService.AnomalyReport orderCountAnomalies;
        public AnomalyDetectionService.AnomalyReport cancellationAnomalies;
        public AnomalyDetectionService.AnomalyReport revenueAnomalies;

        public Map<String, BranchPerformanceService.BranchPerformance> branchPerformance;
        public List<BranchPerformanceService.BranchPerformance> branchRanking;
        public List<BranchPerformanceService.BranchPerformance> underperformingBranches;
    }
}
