package com.bancobase.payments.validators;

import com.bancobase.payments.model.PaymentStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation that ensures the provided
 * {@link PaymentStatus} value is valid.
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentStatusValidator.class)
public @interface ValidPaymentStatus {
    String message() default "Invalid payment status.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
