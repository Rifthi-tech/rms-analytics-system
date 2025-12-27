package com.ubereats.rms.domain;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Gender fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return OTHER;
        }

        for (Gender gender : Gender.values()) {
            if (gender.displayName.equalsIgnoreCase(text) ||
                    gender.name().equalsIgnoreCase(text)) {
                return gender;
            }
        }
        return OTHER;
    }
}