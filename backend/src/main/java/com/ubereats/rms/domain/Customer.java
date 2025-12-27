package com.ubereats.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String customerId;
    private String name;
    private String contactNo;
    private Gender gender;
    private int age;
    private LocalDate joinDate;
    private LoyaltyTier loyaltyGroup;
    private double estimatedTotalSpentLkr;
}