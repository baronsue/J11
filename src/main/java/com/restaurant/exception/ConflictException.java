package com.restaurant.exception;

/**
 * Conflict exception, thrown when business rules are violated (e.g., double booking).
 */
public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message);
    }
}

