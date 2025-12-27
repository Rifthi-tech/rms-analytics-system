package com.ubereats.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeakDiningResult {
    private Map<Integer, Long> ordersByHour;
    private Map<String, Long> ordersByDay;
    private Map<String, Long> ordersByMonth;
    private long totalOrders;
    private double totalRevenue;
}