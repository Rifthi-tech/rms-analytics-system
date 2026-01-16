package com.rms.analytics.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern - Manages dead letter records
 * Ensures only one instance handles all failed records
 */
public class DeadLetterQueue {
    private static DeadLetterQueue instance;
    private final List<String> deadLetters;
    private static final int MAX_RECORDS = 100000;

    private DeadLetterQueue() {
        this.deadLetters = new ArrayList<>();
    }

    /**
     * Get singleton instance - thread-safe implementation
     */
    public static synchronized DeadLetterQueue getInstance() {
        if (instance == null) {
            instance = new DeadLetterQueue();
        }
        return instance;
    }

    /**
     * Add a dead letter record
     */
    public synchronized void addDeadLetter(String record, String reason) {
        if (deadLetters.size() < MAX_RECORDS) {
            deadLetters.add(String.format("[%d] %s - Reason: %s", 
                deadLetters.size() + 1, record, reason));
        }
    }

    /**
     * Get all dead letters
     */
    public synchronized List<String> getDeadLetters() {
        return new ArrayList<>(deadLetters);
    }

    /**
     * Clear all dead letters
     */
    public synchronized void clear() {
        deadLetters.clear();
    }

    /**
     * Get count of dead letters
     */
    public synchronized int getCount() {
        return deadLetters.size();
    }
}
