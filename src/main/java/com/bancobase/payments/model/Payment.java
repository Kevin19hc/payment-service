package com.bancobase.payments.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents a payment document in MongoDB.
 */
@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String id;

    private String concept;

    private Integer quantity;

    private String payerId;

    private String recipientId;

    private String productId;

    private BigDecimal amount;

    private PaymentStatus status;
}
