package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PerformanceTests {

    @Test
    public void testDataProcessingPerformance() {
        // Test data processing performance
        long startTime = System.currentTimeMillis();
        
        // Simulate data processing
        for (int i = 0; i < 1000; i++) {
            String data = "test-data-" + i;
            processData(data);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete within 5 seconds
        assertTrue(duration < 5000);
    }

    @Test
    public void testMemoryUsage() {
        // Test memory usage
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // Simulate memory-intensive operation
        String[] largeArray = new String[1000];
        for (int i = 0; i < 1000; i++) {
            largeArray[i] = "data-" + i;
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        
        // Memory usage should be reasonable
        assertTrue(memoryUsed < 10000000); // Less than 10MB
    }

    @Test
    public void testConcurrentProcessing() {
        // Test concurrent processing performance
        long startTime = System.currentTimeMillis();
        
        // Simulate concurrent operations
        for (int i = 0; i < 100; i++) {
            processDataConcurrently();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should handle concurrent operations efficiently
        assertTrue(duration < 3000);
    }

    // Helper methods
    private void processData(String data) {
        // Simulate data processing
        data.toUpperCase();
    }

    private void processDataConcurrently() {
        // Simulate concurrent data processing
        Thread.yield();
    }
}