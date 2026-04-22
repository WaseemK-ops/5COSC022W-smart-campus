package com.smartcampus.exception;

/**
 * Thrown when a request body references a resource that does not exist.
 * For example, registering a sensor with a roomId that is not in the system.
 * Mapped to HTTP 422 Unprocessable Entity by its exception mapper.
 */



public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message) {
        
        super(message);
    }
}