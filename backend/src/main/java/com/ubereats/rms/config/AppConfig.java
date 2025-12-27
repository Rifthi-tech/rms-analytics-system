package com.ubereats.rms.config;

import com.ubereats.rms.pipeline.*;
import com.ubereats.rms.repository.DataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AppConfig {

    @Value("${data.chunk-size:10000}")
    private int chunkSize;

    @Value("${data.max-file-size:500MB}")
    private String maxFileSize;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Pipeline defaultPipeline() {
        return new DefaultPipeline();
    }

    @Bean
    public FilterStep filterStep() {
        return new FilterStep();
    }

    @Bean
    public EnrichStep enrichStep() {
        return new EnrichStep();
    }

    @Bean
    public TransformStep transformStep() {
        return new TransformStep();
    }

    @Bean
    public AggregateStep aggregateStep() {
        return new AggregateStep(AggregateStep.AggregationType.BY_HOUR);
    }

    @Bean
    public DataSourceFactory dataSourceFactory() {
        return new DataSourceFactory();
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(appName + "-");
        executor.initialize();
        return executor;
    }

    @Bean
    public java.time.format.DateTimeFormatter dateTimeFormatter() {
        return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}