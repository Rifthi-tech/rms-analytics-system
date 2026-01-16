package com.rms.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DeadLetterRecord - for records that fail processing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeadLetterRecord {
    private String recordId;
    private String rawData;
    private String errorReason;
    private long timestamp;
    private String source;
}
