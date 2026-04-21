package com.smartcampus.exception;

/**
 * Thrown when a reading is submitted to a sensor
 * that is currently unavailable, such as a sensor in maintenance mode.
 */
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}