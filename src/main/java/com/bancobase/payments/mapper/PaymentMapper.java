package com.bancobase.payments.mapper;

import com.bancobase.payments.dto.PaymentRequestDTO;
import com.bancobase.payments.dto.PaymentResponseDTO;
import com.bancobase.payments.model.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bancobase.payments.model.PaymentStatus.fromString;

/**
 * Maps between {@link Payment} entities and their DTO representations.
 * Provides conversion methods to transform between request and response
 */
@Component
public class PaymentMapper {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converts a {@link PaymentRequestDTO} into a {@link Payment} entity.
     *
     * @param dto the object containing payment information.
     * @return the mapped {@link Payment} entity, or null if the source is null.
     */
    public Payment toEntity(PaymentRequestDTO dto) {
        if (dto == null) return null;
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(fromString(dto.getStatus()));
        return payment;
    }

    /**
     * Converts a {@link Payment} entity into a {@link PaymentResponseDTO}.
     *
     * @param entity the payment entity to map.
     * @return the mapped {@link PaymentResponseDTO}, or null if the source is null.
     */
    public PaymentResponseDTO toResponse(Payment entity) {
        if (entity == null) return null;
        PaymentResponseDTO responseDTO = modelMapper.map(entity, PaymentResponseDTO.class);
        responseDTO.setStatus(entity.getStatus().toStringValue());
        return responseDTO;
    }
}
