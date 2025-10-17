package com.bancobase.payments.service;

import com.bancobase.payments.model.Payment;
import com.bancobase.payments.model.PaymentStatus;
import com.bancobase.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentProducerService paymentProducerService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        openMocks(this);

        payment = new Payment();
        payment.setId("abc123");
        payment.setPayerId("TestPayer");
        payment.setRecipientId("TestRecipient");
        payment.setAmount(BigDecimal.valueOf(120.50));
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Should create a payment successfully")
    void testCreatePayment_shouldReturnPayment_whenDataIsValid() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("abc123");
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.PENDING);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    @DisplayName("Should return all payments")
    void testGetAllPayments_shouldReturnList_whenPaymentsExist() {
        when(paymentRepository.findAll()).thenReturn(Collections.singletonList(payment));

        List<Payment> result = paymentService.getAllPayments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipientId()).isEqualTo("TestRecipient");
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return payment by id")
    void testGetPaymentById_shouldReturnPayment_whenIdExists() {
        when(paymentRepository.findById("abc123")).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById("abc123");

        assertThat(result).isNotNull();
        assertThat(result.getPayerId()).isEqualTo("TestPayer");
        verify(paymentRepository, times(1)).findById("abc123");
    }

    @Test
    @DisplayName("Should throw exception when payment not found by id")
    void testGetPaymentById_shouldThrowException_whenPaymentDoesNotExist() {
        when(paymentRepository.findById("missingId")).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () ->
                paymentService.getPaymentById("missingId")
        );

        verify(paymentRepository, times(1)).findById("missingId");
    }

    @Test
    @DisplayName("Should update status and publish event successfully")
    void testUpdateStatus_shouldUpdatePayment_whenPaymentExists() {
        when(paymentRepository.findById("abc123")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.updateStatus("abc123", "COMPLETED");

        assertThat(result.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentProducerService, times(1)).publish(payment);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent payment")
    void testUpdateStatus_shouldThrowException_whenPaymentNotFound() {
        when(paymentRepository.findById("xyz")).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () ->
                paymentService.updateStatus("xyz", "FAILED")
        );

        verify(paymentRepository, never()).save(any());
        verify(paymentProducerService, never()).publish(any());
    }
}