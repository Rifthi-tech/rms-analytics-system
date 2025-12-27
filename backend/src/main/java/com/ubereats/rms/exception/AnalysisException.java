package com.ubereats.rms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AnalysisException extends RuntimeException {

    private final String analysisType;
    private final String module;

    public AnalysisException(String message) {
        super(message);
        this.analysisType = "UNKNOWN";
        this.module = "UNKNOWN";
    }

    public AnalysisException(String analysisType, String module, String message) {
        super(String.format("[%s/%s] %s", analysisType, module, message));
        this.analysisType = analysisType;
        this.module = module;
    }

    public AnalysisException(String analysisType, String module, String message, Throwable cause) {
        super(String.format("[%s/%s] %s", analysisType, module, message), cause);
        this.analysisType = analysisType;
        this.module = module;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public String getModule() {
        return module;
    }
}