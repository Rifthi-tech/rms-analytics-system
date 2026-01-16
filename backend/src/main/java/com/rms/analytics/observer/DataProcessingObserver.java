package com.rms.analytics.observer;

/**
 * Observer Interface - Observer Pattern
 * Allows analytics engines to observe data processing events
 */
public interface DataProcessingObserver {
    void onChunkProcessed(int recordsProcessed, int totalRecords);
    void onProcessingComplete(long elapsedTimeMs);
    void onErrorOccurred(String errorMessage);
}
