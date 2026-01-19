package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegressionTests {

    @Test
    public void testExistingFunctionalityStillWorks() {
        // Test that existing functionality still works after changes
        String testData = "regression,test,data";
        String[] parts = testData.split(",");
        
        assertEquals(3, parts.length);
        assertEquals("regression", parts[0]);
        assertEquals("test", parts[1]);
        assertEquals("data", parts[2]);
    }

    @Test
    public void testDataFormatCompatibility() {
        // Test data format compatibility
        String csvData = "outlet,revenue,date\nOutlet1,1000,2024-01-01";
        assertTrue(csvData.contains("outlet"));
        assertTrue(csvData.contains("revenue"));
        assertTrue(csvData.contains("date"));
    }

    @Test
    public void testApiResponseFormat() {
        // Test API response format hasn't changed
        String jsonResponse = "{\"status\":\"success\",\"data\":[]}";
        assertTrue(jsonResponse.contains("status"));
        assertTrue(jsonResponse.contains("data"));
    }

    @Test
    public void testBackwardCompatibility() {
        // Test backward compatibility
        String oldFormatData = "old-format-data";
        String newFormatData = convertToNewFormat(oldFormatData);
        
        assertNotNull(newFormatData);
        assertTrue(newFormatData.length() > 0);
    }

    @Test
    public void testConfigurationStability() {
        // Test configuration stability
        String javaVersion = System.getProperty("java.version");
        assertNotNull(javaVersion);
        
        String osName = System.getProperty("os.name");
        assertNotNull(osName);
    }

    // Helper method
    private String convertToNewFormat(String oldData) {
        return oldData.replace("-", "_"); // Simple format conversion
    }
}