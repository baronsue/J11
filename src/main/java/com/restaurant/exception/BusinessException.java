package com.restaurant.exception;

/**
 * Business exception base class, all business-related exceptions inherit from this class.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

