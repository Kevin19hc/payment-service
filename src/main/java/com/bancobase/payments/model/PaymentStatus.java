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
    FAILED;

    /**
     * Converts a string value to its corresponding PaymentStatus enum constant.
     *
     * @param value the string representation of the payment status
     * @return the corresponding PaymentStatus constant
     * @throws IllegalArgumentException if the provided value does not match any status
     */
    public static PaymentStatus fromString(String value) {
        try {
            return PaymentStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + value);
        }
    }

    /**
     * Returns the string representation of the current PaymentStatus constant.
     *
     * @return the name of this enum constant as a string
     */
    public String toStringValue() {
        return this.name();
    }
}
