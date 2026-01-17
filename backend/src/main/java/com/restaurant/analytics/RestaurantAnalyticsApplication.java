package com.restaurant.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RestaurantAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantAnalyticsApplication.class, args);
    }
}