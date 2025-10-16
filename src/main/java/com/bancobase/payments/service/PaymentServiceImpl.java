package com.bancobase.payments.service;

import com.bancobase.payments.model.Payment;
import com.bancobase.payments.model.PaymentStatus;
import com.bancobase.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Payment not found with id: " + id, 1));
    }

    @Override
    public Payment updateStatus(String id, String newStatus) {
        Payment payment = getPaymentById(id);
        payment.setStatus(PaymentStatus.fromString(newStatus));
        Payment updatedPayment = paymentRepository.save(payment);

        return updatedPayment;
    }
}
