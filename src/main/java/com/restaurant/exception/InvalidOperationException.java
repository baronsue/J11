package com.restaurant.exception;

/**
 * Invalid operation exception, thrown when attempting to perform an invalid operation.
 */
public class InvalidOperationException extends BusinessException {

    public InvalidOperationException(String message) {
        super(message);
    }
}

