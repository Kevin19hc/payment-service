package com.bancobase.payments.service;

import com.bancobase.payments.model.Payment;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * Service for managing payments.
 */
public interface PaymentService {

    /**
     * Creates a new payment record.
     *
     * @param payment the payment to create
     * @return the saved payment document
     */
    Payment createPayment(Payment payment);

    /**
     * Retrieves all payments from the database.
     *
     * @return a list of all payments
     */
    List<Payment> getAllPayments();

    /**
     * Retrieves a payment by its id.
     *
     * @param id the payment identifier
     * @return the payment document
     * @throws EmptyResultDataAccessException if the payment is not found
     */
    Payment getPaymentById(String id);

    /**
     * Updates the status of an existing payment.
     *
     * @param id the identifier of the payment to update
     * @param newStatus the new status to assign
     * @return the updated payment document
     */
    Payment updateStatus(String id, String newStatus);
}
