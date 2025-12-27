package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import java.util.List;

public interface Pipeline {
    Pipeline addStep(PipelineStep step);
    List<Order> execute(List<Order> input);
    PipelineResult getResult();
}

class PipelineResult {
    private long processingTime;
    private int inputCount;
    private int outputCount;
    private List<String> errors;

    // Getters and setters
}