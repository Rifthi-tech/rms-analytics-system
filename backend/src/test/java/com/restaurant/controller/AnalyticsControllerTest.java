package com.restaurant.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Analytics Controller Unit Tests")
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test GET /api/health endpoint")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test GET /api/analytics/revenue endpoint")
    void testRevenueAnalyticsEndpoint() throws Exception {
        mockMvc.perform(get("/api/analytics/revenue"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test GET /api/analytics/peak-dining endpoint")
    void testPeakDiningEndpoint() throws Exception {
        mockMvc.perform(get("/api/analytics/peak-dining"))
                .andExpect(status().isOk());
    }
}
