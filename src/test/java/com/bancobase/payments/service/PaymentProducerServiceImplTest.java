package com.bancobase.payments.service;

import com.bancobase.payments.model.Payment;
import com.bancobase.payments.model.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


class PaymentProducerServiceImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentProducerServiceImpl paymentProducerService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        openMocks(this);

        ReflectionTestUtils.setField(paymentProducerService, "exchangeName", "test-exchange");

        payment = new Payment();
        payment.setId("p123");
        payment.setPayer("TestPayer");
        payment.setRecipient("TestRecipient");
        payment.setAmount(BigDecimal.valueOf(250.75));
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Should publish payment event successfully when RabbitMQ is available")
    void testPublish_shouldSendMessage_whenRabbitMQIsAvailable() {
        paymentProducerService.publish(payment);

        verify(rabbitTemplate, times(1))
                .convertAndSend("test-exchange", "", payment);
    }

    @Test
    @DisplayName("Should handle exception gracefully when RabbitMQ fails")
    void testPublish_shouldHandleError_whenRabbitMQThrowsException() {
        doThrow(new RuntimeException("Connection error"))
                .when(rabbitTemplate)
                .convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        paymentProducerService.publish(payment);

        verify(rabbitTemplate, times(1))
                .convertAndSend("test-exchange", "", payment);
    }
}
