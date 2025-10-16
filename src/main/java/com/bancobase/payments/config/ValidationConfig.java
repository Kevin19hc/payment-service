package com.bancobase.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Configuration class for method-level validation.
 */
@Configuration
public class ValidationConfig {

    /**
     * Registers a post-processor that enables
     * method parameter validation.
     *
     * @return the MethodValidationPostProcessor instance
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
