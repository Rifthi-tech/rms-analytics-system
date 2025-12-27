package com.ubereats.rms.domain;

public enum LoyaltyTier {
    BRONZE("Bronze", 0, 9999),
    SILVER("Silver", 10000, 24999),
    GOLD("Gold", 25000, 49999),
    PLATINUM("Platinum", 50000, Double.MAX_VALUE);

    private final String displayName;
    private final double minSpend;
    private final double maxSpend;

    LoyaltyTier(String displayName, double minSpend, double maxSpend) {
        this.displayName = displayName;
        this.minSpend = minSpend;
        this.maxSpend = maxSpend;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMinSpend() {
        return minSpend;
    }

    public double getMaxSpend() {
        return maxSpend;
    }

    public static LoyaltyTier fromSpendAmount(double totalSpend) {
        for (LoyaltyTier tier : values()) {
            if (totalSpend >= tier.minSpend && totalSpend <= tier.maxSpend) {
                return tier;
            }
        }
        return BRONZE;
    }

    public static LoyaltyTier fromString(String text) {
        for (LoyaltyTier tier : values()) {
            if (tier.displayName.equalsIgnoreCase(text) ||
                    tier.name().equalsIgnoreCase(text)) {
                return tier;
            }
        }
        return BRONZE;
    }
}