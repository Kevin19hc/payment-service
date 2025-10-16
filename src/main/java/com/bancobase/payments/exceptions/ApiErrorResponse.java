package com.bancobase.payments.exceptions;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Standard API error response model for REST endpoints.
 */
@Getter
@Builder
public class ApiErrorResponse {

    private final int status;
    private final String message;
    private final List<FieldError> errors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}
