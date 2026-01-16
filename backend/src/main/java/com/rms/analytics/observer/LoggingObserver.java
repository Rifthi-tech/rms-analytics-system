package com.rms.analytics.observer;

/**
 * Concrete Observer - Logs data processing events
 */
public class LoggingObserver implements DataProcessingObserver {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingObserver.class);

    @Override
    public void onChunkProcessed(int recordsProcessed, int totalRecords) {
        double percentage = (double) recordsProcessed / totalRecords * 100;
        logger.info("Processed {} records ({:.2f}%)", recordsProcessed, percentage);
    }

    @Override
    public void onProcessingComplete(long elapsedTimeMs) {
        logger.info("Data processing completed in {} ms", elapsedTimeMs);
    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        logger.error("Processing error: {}", errorMessage);
    }
}
