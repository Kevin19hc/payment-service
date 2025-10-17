package com.bancobase.payments.config;

import com.bancobase.payments.dto.PaymentRequestDTO;
import com.bancobase.payments.model.Payment;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(PaymentRequestDTO.class, Payment.class)
                .addMappings(mapper -> mapper.skip(Payment::setId));

        return modelMapper;
    }
}
