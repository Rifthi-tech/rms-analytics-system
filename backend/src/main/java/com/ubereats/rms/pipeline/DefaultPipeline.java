package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultPipeline implements Pipeline {

    private final List<PipelineStep> steps = new ArrayList<>();
    private PipelineResult result = new PipelineResult();

    @Override
    public Pipeline addStep(PipelineStep step) {
        steps.add(step);
        return this;
    }

    @Override
    public List<Order> execute(List<Order> input) {
        long startTime = System.currentTimeMillis();

        List<Order> currentData = new ArrayList<>(input);

        for (PipelineStep step : steps) {
            try {
                System.out.println("Executing step: " + step.getName());
                currentData = step.process(currentData);
            } catch (Exception e) {
                System.err.println("Error in step " + step.getName() + ": " + e.getMessage());
                result.getErrors().add("Step " + step.getName() + " failed: " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        result.setProcessingTime(endTime - startTime);
        result.setInputCount(input.size());
        result.setOutputCount(currentData.size());

        return currentData;
    }

    @Override
    public PipelineResult getResult() {
        return result;
    }
}