package com.bancobase.payments.controller;

import com.bancobase.payments.dto.PaymentRequestDTO;
import com.bancobase.payments.dto.PaymentResponseDTO;
import com.bancobase.payments.mapper.PaymentMapper;
import com.bancobase.payments.model.Payment;
import com.bancobase.payments.service.PaymentService;
import com.bancobase.payments.validators.ValidPaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Operation(
            summary = "Create a new payment",
            description = """
                    Creates a new payment record in the system.
                    Validates the fields and persists it into the database.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Payment details to create",
                    content = @Content(
                            schema = @Schema(implementation = PaymentRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Payment example",
                                    value = """
                                            {
                                              "concept": "Invoice #12345",
                                              "quantity": 1,
                                              "payer": "Juan Pérez",
                                              "recipient": "Banco Base",
                                              "amount": 1200.50,
                                              "status": "PENDING"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Payment successfully created",
                            content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        Payment created = paymentService.createPayment(paymentMapper.toEntity(request));
        return ResponseEntity.ok(paymentMapper.toResponse(created));
    }

    @Operation(
            summary = "Retrieve all payments",
            description = "Returns a list of all payments stored in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of payments retrieved",
                            content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<PaymentResponseDTO> responses = payments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get payment by ID",
            description = "Fetches the details of a specific payment using its ID.",
            parameters = {
                    @Parameter(name = "id", description = "Unique identifier of the payment", example = "1",
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment found",
                            content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable String id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(paymentMapper.toResponse(payment));
    }

    @Operation(
            summary = "Update payment status",
            description = """
            Updates only the status of an existing payment.
            The new status must be one of the valid values (PENDING, PROCESSING, COMPLETED, FAILED).
            """,
            parameters = {
                    @Parameter(name = "id", description = "Unique ID of the payment", example = "1", required = true),
                    @Parameter(name = "status", description = "New status value", example = "COMPLETED", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment status updated successfully",
                            content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(@PathVariable String id,
                                                                  @ValidPaymentStatus @RequestParam String status) {
        Payment updated = paymentService.updateStatus(id, status);
        return ResponseEntity.ok(paymentMapper.toResponse(updated));
    }
}
