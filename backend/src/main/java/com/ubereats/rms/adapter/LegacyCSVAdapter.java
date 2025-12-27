package com.ubereats.rms.adapter;

import com.ubereats.rms.domain.Order;
import com.ubereats.rms.exception.DataIngestionException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LegacyCSVAdapter extends CSVSchemaAdapter {

    // Specific adapter for legacy CSV formats
    private static final Map<String, String> LEGACY_V2_MAPPING = Map.of(
            "order_id", "TRANSACTION_ID",
            "customer_id", "CLIENT_ID",
            "outlet_id", "LOCATION_CODE",
            "order_placed", "TRANSACTION_DATE",
            "total_price_lkr", "AMOUNT",
            "status", "TRANSACTION_STATUS",
            "payment_method", "PAYMENT_TYPE"
    );

    public LegacyCSVAdapter() {
        super();
        // Register legacy schema
        schemaMappings.put("LEGACY_V2", LEGACY_V2_MAPPING);
    }

    public List<Order> adaptLegacyFormat(List<String[]> legacyRecords, String formatVersion) {
        try {
            if (legacyRecords.isEmpty()) {
                return List.of();
            }

            String[] headers = legacyRecords.get(0);
            Map<String, Integer> headerMap = validateAndMapHeaders(headers, formatVersion);

            return legacyRecords.stream()
                    .skip(1) // Skip header
                    .map(record -> adaptToOrder(record, headerMap))
                    .toList();

        } catch (Exception e) {
            throw new DataIngestionException(
                    String.format("Failed to adapt legacy CSV format '%s'", formatVersion), e);
        }
    }

    public boolean canHandleFormat(String fileName, String[] headers) {
        // Check if this is a legacy format based on file name or headers
        if (fileName != null) {
            String lowerName = fileName.toLowerCase();
            if (lowerName.contains("legacy") ||
                    lowerName.contains("old") ||
                    lowerName.contains("v1") ||
                    lowerName.contains("v2")) {
                return true;
            }
        }

        // Check headers for legacy patterns
        for (String header : headers) {
            String lowerHeader = header.toLowerCase();
            if (lowerHeader.contains("trans") ||
                    lowerHeader.contains("client") ||
                    lowerHeader.contains("location")) {
                return true;
            }
        }

        return false;
    }

    public String detectFormat(String[] headers) {
        // Auto-detect format based on headers
        int uberEatsMatch = 0;
        int legacyV1Match = 0;
        int legacyV2Match = 0;

        for (String header : headers) {
            String lowerHeader = header.trim().toLowerCase();

            // Check for Uber Eats standard headers
            if (EXPECTED_HEADERS.contains(lowerHeader)) {
                uberEatsMatch++;
            }

            // Check for legacy V1 patterns
            if (lowerHeader.contains("orderid") ||
                    lowerHeader.contains("customerid") ||
                    lowerHeader.contains("storeid")) {
                legacyV1Match++;
            }

            // Check for legacy V2 patterns
            if (lowerHeader.contains("transaction") ||
                    lowerHeader.contains("client") ||
                    lowerHeader.contains("location")) {
                legacyV2Match++;
            }
        }

        // Determine best match
        if (uberEatsMatch >= legacyV1Match && uberEatsMatch >= legacyV2Match) {
            return "UBER_EATS_STANDARD";
        } else if (legacyV2Match >= legacyV1Match) {
            return "LEGACY_V2";
        } else {
            return "LEGACY_V1";
        }
    }
}