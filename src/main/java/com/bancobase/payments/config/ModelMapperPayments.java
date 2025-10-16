package com.bancobase.payments.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global configuration class for {@link ModelMapper}.
 * Defines strict and safe mapping rules to avoid ambiguous property matches between DTOs.
 */
@Configuration
public class ModelMapperPayments {

    /**
     * Provides a configured {@link ModelMapper} bean for object mapping
     * throughout the application.
     *
     * @return a {@link ModelMapper} instance.
     */
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
