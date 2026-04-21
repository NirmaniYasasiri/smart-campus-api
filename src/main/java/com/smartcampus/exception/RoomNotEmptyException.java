package com.smartcampus.exception;

/**
 * Thrown when a client tries to delete a room that still has sensors assigned to it.
 */
public class RoomNotEmptyException extends RuntimeException {

    public RoomNotEmptyException(String message) {
        super(message);
    }
}