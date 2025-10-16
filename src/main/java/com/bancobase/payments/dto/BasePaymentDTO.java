package com.bancobase.payments.dto;

import com.bancobase.payments.validators.ValidPaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Base DTO containing shared fields and validation rules
 * for all payment-related data transfer objects.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class BasePaymentDTO {

    @Schema(description = "Description or concept of the payment.", example = "Monthly subscription")
    @NotBlank
    protected String concept;

    @Schema(description = "Number of items or units involved in the payment.", example = "2")
    @NotNull
    protected Integer quantity;

    @Schema(description = "Identifier of the payer.", example = "user123")
    @NotBlank
    protected String payer;

    @Schema(description = "Identifier of the recipient.", example = "Vendor123")
    @NotBlank
    protected String recipient;

    @Schema(description = "Amount of the payment. Must be positive.", example = "199.99")
    @NotNull
    @Positive
    protected BigDecimal amount;

    @Schema(description = "Current status of the payment.", example = "PENDING")
    @NotNull
    @ValidPaymentStatus
    protected String status;
}
