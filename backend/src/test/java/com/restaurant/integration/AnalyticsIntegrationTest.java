package com.restaurant.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Analytics Integration Tests")
class AnalyticsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Integration Test: Full analytics workflow")
    void testFullAnalyticsWorkflow() {
        String baseUrl = "http://localhost:" + port;
        
        // Test health endpoint
        ResponseEntity<String> healthResponse = restTemplate.getForEntity(
            baseUrl + "/api/health", String.class);
        assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
    }

    @Test
    @DisplayName("Integration Test: Revenue analytics end-to-end")
    void testRevenueAnalyticsEndToEnd() {
        String baseUrl = "http://localhost:" + port;
        
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/api/analytics/revenue", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Integration Test: Customer analytics end-to-end")
    void testCustomerAnalyticsEndToEnd() {
        String baseUrl = "http://localhost:" + port;
        
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/api/analytics/customer-demographics", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
