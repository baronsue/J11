package com.restaurant.exception;

/**
 * Resource not found exception, thrown when requested resource does not exist.
 */
public class ResourceNotFoundException extends BusinessException {

    private static final String DEFAULT_MESSAGE_TEMPLATE = "%s not found with %s: %s";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, resourceName, fieldName, fieldValue));
    }
}

