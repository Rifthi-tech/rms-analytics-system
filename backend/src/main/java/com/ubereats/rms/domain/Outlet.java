package com.ubereats.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outlet {
    private String outletId;
    private String name;
    private String borough;
    private int capacity;
    private LocalDateTime opened;
}