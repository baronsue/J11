package com.restaurant.exception;

/**
 * Validation exception, thrown when input validation fails.
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }
}

