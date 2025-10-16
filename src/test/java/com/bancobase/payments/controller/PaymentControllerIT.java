package com.bancobase.payments.controller;

import com.bancobase.payments.model.Payment;
import com.bancobase.payments.model.PaymentStatus;
import com.bancobase.payments.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should create a payment successfully when the request is valid")
    void createPayment_shouldCreatePaymentSuccessfully_whenRequestIsValid() throws Exception {
        String json = buildPaymentJson(paymentBuilder -> paymentBuilder.quantity(1));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept", is("Invoice #123")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    @DisplayName("Should return all existing payments")
    void getAllPayments_shouldReturnAllPayments_whenPaymentsExist() throws Exception {
        repository.save(Payment.builder()
                .concept("Invoice #A")
                .quantity(1)
                .payer("Payer test")
                .recipient("Recipient test")
                .amount(new BigDecimal("999.99"))
                .status(PaymentStatus.COMPLETED)
                .build());

        mockMvc.perform(get("/api/v1/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("COMPLETED")));
    }

    @Test
    @DisplayName("Should return a payment by ID when it exists")
    void getPaymentById_shouldReturnPayment_whenExists() throws Exception {
        Payment p = repository.save(Payment.builder()
                .concept("Invoice #77")
                .quantity(1)
                .payer("Payer test")
                .recipient("Recipient test")
                .amount(new BigDecimal("1200.00"))
                .status(PaymentStatus.PENDING)
                .build());

        mockMvc.perform(get("/api/v1/payments/{id}", p.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept", is("Invoice #77")));
    }

    @Test
    @DisplayName("Should update the payment status when a valid status is provided")
    void updatePaymentStatus_shouldUpdate_whenValidStatusProvided() throws Exception {
        Payment p = repository.save(Payment.builder()
                .concept("Invoice #88")
                .quantity(1)
                .payer("Payer test")
                .recipient("Banco Base")
                .amount(new BigDecimal("500.00"))
                .status(PaymentStatus.PENDING)
                .build());

        mockMvc.perform(patch("/api/v1/payments/{id}?status=COMPLETED", p.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    @DisplayName("Should fail to create a payment when the 'concept' field is empty")
    void createPayment_shouldFail_whenConceptIsMissing() throws Exception {
        String json = buildPaymentJson(p -> p.concept(""));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail to create a payment when the amount is negative")
    void createPayment_shouldFail_whenAmountIsNegative() throws Exception {
        String json = buildPaymentJson(p -> p.amount(BigDecimal.valueOf(-10)));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail to create a payment when the quantity is zero or null")
    void createPayment_shouldFail_whenQuantityIsZero() throws Exception {
        String json = buildPaymentJson(p -> p.quantity(null));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail to create a payment when the status is invalid")
    void createPayment_shouldFail_whenStatusIsInvalid() throws Exception {
        String json = """
        {
          "concept": "Invoice #Z",
          "quantity": 1,
          "payer": "Payer test",
          "recipient": "Recipient test",
          "amount": 800.00,
          "status": "INVALID_STATUS"
        }
        """;

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail to create a payment when all required fields are missing")
    void createPayment_shouldFail_whenAllFieldsMissing() throws Exception {
        String json = objectMapper.writeValueAsString(new Payment());

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    private String buildPaymentJson(Consumer<Payment.PaymentBuilder> overrides) throws Exception {
        Payment.PaymentBuilder builder = Payment.builder()
                .concept("Invoice #123")
                .quantity(2)
                .payer("Payer test")
                .recipient("Recipient test")
                .amount(BigDecimal.valueOf(1500.75))
                .status(PaymentStatus.PENDING);

        overrides.accept(builder);
        return objectMapper.writeValueAsString(builder.build());
    }
}