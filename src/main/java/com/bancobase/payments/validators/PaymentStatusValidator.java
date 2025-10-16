package com.bancobase.payments.validators;

import com.bancobase.payments.model.PaymentStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for {@link ValidPaymentStatus}.
 * Ensures that the provided {@link PaymentStatus} value
 * corresponds to one of the defined constants in the enumeration.
 */
public class PaymentStatusValidator implements ConstraintValidator<ValidPaymentStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            PaymentStatus.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
