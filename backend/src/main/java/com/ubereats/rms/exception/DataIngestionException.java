package com.ubereats.rms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIngestionException extends RuntimeException {

    public DataIngestionException(String message) {
        super(message);
    }

    public DataIngestionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIngestionException(String fileName, String error, Throwable cause) {
        super(String.format("Failed to ingest data from file '%s': %s", fileName, error), cause);
    }

    public DataIngestionException(int lineNumber, String error) {
        super(String.format("Error at line %d: %s", lineNumber, error));
    }
}package com.ubereats.rms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIngestionException extends RuntimeException {

    public DataIngestionException(String message) {
        super(message);
    }

    public DataIngestionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIngestionException(String fileName, String error, Throwable cause) {
        super(String.format("Failed to ingest data from file '%s': %s", fileName, error), cause);
    }

    public DataIngestionException(int lineNumber, String error) {
        super(String.format("Error at line %d: %s", lineNumber, error));
    }
}