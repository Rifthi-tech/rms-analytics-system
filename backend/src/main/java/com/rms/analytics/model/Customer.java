package com.rms.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Customer entity representing restaurant customer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String customerId;
    private String contactNo;
    private String gender;
    private int age;
    private LocalDate joinDate;
    private String loyaltyGroup;
    private double estimatedTotalSpent;
}
