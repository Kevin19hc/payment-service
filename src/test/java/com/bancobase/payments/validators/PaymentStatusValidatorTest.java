package com.bancobase.payments.validators;

import com.bancobase.payments.model.PaymentStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PaymentStatusValidatorTest {

    private PaymentStatusValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PaymentStatusValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("Should return true when value is null")
    void testIsValid_shouldReturnTrue_whenValueIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Should return true when value matches a valid PaymentStatus (case-insensitive)")
    void testIsValid_shouldReturnTrue_whenValueIsValidEnumNameIgnoringCase() {
        assertTrue(validator.isValid(PaymentStatus.PENDING.name(), context));
        assertTrue(validator.isValid("completed", context));
        assertTrue(validator.isValid("FaIlEd", context));
    }

    @Test
    @DisplayName("Should return false when value does not match any PaymentStatus constant")
    void testIsValid_shouldReturnFalse_whenValueIsInvalidEnumName() {
        assertFalse(validator.isValid("INVALID_STATUS", context));
        assertFalse(validator.isValid("Done", context));
        assertFalse(validator.isValid("", context));
    }
}
