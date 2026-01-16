package com.rms.analytics.util;

import com.rms.analytics.observer.DataProcessingObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern - Central event manager for data processing
 */
public class EventManager {
    private static EventManager instance;
    private final List<DataProcessingObserver> observers;

    private EventManager() {
        this.observers = new ArrayList<>();
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void subscribe(DataProcessingObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(DataProcessingObserver observer) {
        observers.remove(observer);
    }

    public void notifyChunkProcessed(int recordsProcessed, int totalRecords) {
        observers.forEach(observer -> observer.onChunkProcessed(recordsProcessed, totalRecords));
    }

    public void notifyProcessingComplete(long elapsedTimeMs) {
        observers.forEach(observer -> observer.onProcessingComplete(elapsedTimeMs));
    }

    public void notifyError(String errorMessage) {
        observers.forEach(observer -> observer.onErrorOccurred(errorMessage));
    }
}
