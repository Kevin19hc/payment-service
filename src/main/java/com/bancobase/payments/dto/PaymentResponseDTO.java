package com.bancobase.payments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(description = "DTO object for payment responses.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PaymentResponseDTO extends BasePaymentDTO {

    @Schema(description = "Unique identifier of the payment.", example = "64c8a2f6d9e5b01b8c3a9f12")
    private String id;
}
