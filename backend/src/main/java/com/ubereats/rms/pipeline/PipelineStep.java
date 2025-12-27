package com.ubereats.rms.pipeline;

import com.ubereats.rms.domain.Order;
import java.util.List;

public interface PipelineStep {
    String getName();
    List<Order> process(List<Order> input);
}