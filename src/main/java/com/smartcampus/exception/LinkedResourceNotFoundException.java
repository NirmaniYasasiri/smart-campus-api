package com.smartcampus.exception;

/**
 * Thrown when a request refers to another resource that does not exist, such as a sensor using an invalid roomId.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}