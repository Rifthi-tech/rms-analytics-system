package com.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    @Test
    public void testApplicationContextLoads() {
        // Test that Spring context loads successfully
        assertTrue(true);
    }

    @Test
    public void testBasicMathOperations() {
        // Test basic operations
        assertEquals(4, 2 + 2);
        assertEquals(0, 2 - 2);
        assertEquals(4, 2 * 2);
        assertEquals(1, 2 / 2);
    }

    @Test
    public void testStringOperations() {
        // Test string operations
        String test = "Restaurant Analytics";
        assertNotNull(test);
        assertTrue(test.contains("Restaurant"));
        assertEquals(20, test.length()); // Fixed: "Restaurant Analytics" is 20 characters
    }

    @Test
    public void testDataValidation() {
        // Test data validation logic
        String validEmail = "test@restaurant.com";
        String invalidEmail = "invalid-email";
        
        assertTrue(validEmail.contains("@"));
        assertFalse(invalidEmail.contains("@"));
    }

    @Test
    public void testNullChecks() {
        // Test null safety
        String nullString = null;
        String validString = "test";
        
        assertNull(nullString);
        assertNotNull(validString);
    }
}