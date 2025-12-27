package com.ubereats.rms.domain;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    FAILED("Failed");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED || this == REFUNDED;
    }

    public boolean isActive() {
        return this == CONFIRMED || this == PREPARING || this == READY;
    }

    public static OrderStatus fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return PENDING;
        }

        for (OrderStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(text) ||
                    status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return PENDING;
    }
}