package com.bancobase.payments.model;

/**
 * Defines the possible states a payment can have during its lifecycle.
 * - PENDING: The payment has been created but not yet processed.
 * - PROCESSING: The payment is currently being verified or sent.
 * - COMPLETED: The payment has been successfully processed.
 * - FAILED: The payment processing encountered an error.
 */
public enum PaymentStatus {

    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
