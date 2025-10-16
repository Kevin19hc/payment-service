package com.bancobase.payments.service;

import com.bancobase.payments.model.Payment;

/**
 * Defines the contract for publishing payment messages to RabbitMQ.
 */
public interface PaymentProducerService {

    /**
     * Publishes a payment message to RabbitMQ.
     *
     * @param event the payment event to send.
     */
    void publish(Payment event);
}
