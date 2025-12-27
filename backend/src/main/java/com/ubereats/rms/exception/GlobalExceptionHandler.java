package com.ubereats.rms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIngestionException.class)
    public ResponseEntity<String> handleDataIngestionException(DataIngestionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Data ingestion error: " + e.getMessage());
    }

    @ExceptionHandler(AnalysisException.class)
    public ResponseEntity<String> handleAnalysisException(AnalysisException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Analysis error: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}