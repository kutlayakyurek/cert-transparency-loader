package com.ka.cert.transparency.loader.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

/**
 * Description: Spring Boot application configuration
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.application
 * Author: kakyurek
 * Date: 2018.01.07
 */
@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Bean
    public static TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
