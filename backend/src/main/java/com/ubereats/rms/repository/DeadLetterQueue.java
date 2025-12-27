package com.ubereats.rms.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DeadLetterQueue {

    private static DeadLetterQueue instance;
    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    @Value("${data.dead-letter-path}")
    private String deadLetterPath;

    private DeadLetterQueue() {}

    public static synchronized DeadLetterQueue getInstance() {
        if (instance == null) {
            instance = new DeadLetterQueue();
        }
        return instance;
    }

    public void addRecord(String[] record, String errorMessage) {
        String dlqEntry = String.format("%s | %s | %s%n",
                LocalDateTime.now(),
                String.join(",", record),
                errorMessage);
        queue.add(dlqEntry);

        // Flush to file if queue reaches certain size
        if (queue.size() >= 1000) {
            flushToFile();
        }
    }

    public void flushToFile() {
        try (FileWriter writer = new FileWriter(deadLetterPath + "dead_letter_" +
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv")) {
            String entry;
            while ((entry = queue.poll()) != null) {
                writer.write(entry);
            }
        } catch (IOException e) {
            System.err.println("Failed to write dead letter queue: " + e.getMessage());
        }
    }
}