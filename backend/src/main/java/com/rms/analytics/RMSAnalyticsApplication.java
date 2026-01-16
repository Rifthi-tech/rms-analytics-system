package com.rms.analytics;

import com.rms.analytics.observer.LoggingObserver;
import com.rms.analytics.util.EventManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Spring Boot Application
 * RMS Analytics System - Restaurant Management System Data Analytics Tool
 */
@SpringBootApplication
public class RMSAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RMSAnalyticsApplication.class, args);
    }

    /**
     * Configure CORS for frontend integration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }

    /**
     * Initialize event observers for data processing
     */
    @Bean
    public void initializeObservers() {
        EventManager.getInstance().subscribe(new LoggingObserver());
    }
}
