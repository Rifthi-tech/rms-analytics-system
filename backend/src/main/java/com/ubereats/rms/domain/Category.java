package com.ubereats.rms.domain;

public enum Category {
    APPETIZER("Appetizer"),
    MAIN_COURSE("Main Course"),
    DESSERT("Dessert"),
    BEVERAGE("Beverage"),
    SIDE_DISH("Side Dish"),
    SOUP("Soup"),
    SALAD("Salad"),
    RICE("Rice"),
    NOODLES("Noodles"),
    BREAD("Bread"),
    CONDIMENT("Condiment");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.displayName.equalsIgnoreCase(text) ||
                    category.name().equalsIgnoreCase(text)) {
                return category;
            }
        }
        return null;
    }
}