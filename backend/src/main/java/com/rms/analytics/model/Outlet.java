package com.rms.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Outlet entity representing restaurant branch/outlet
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outlet {
    private String outletId;
    private String name;
    private String borough;
    private int capacity;
    private LocalDate opened;
}
