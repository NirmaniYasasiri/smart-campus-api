package com.smartcampus.model;

/**
 * Standard JSON error response model used by the API.
 * This ensures errors are returned in a clean and consistent format instead of exposing raw server-side exceptions.
 */
public class ApiError {

    /** HTTP status code */
    private int status;

    /** Short error name */
    private String error;

    /** Human-readable error message */
    private String message;

    /** Time when the error was created */
    private long timestamp;

    /** Default constructor for JSON support */
    public ApiError() {
    }

    /**
     * Creates a standard API error response.
     *
     * @param status HTTP status code
     * @param error short error name
     * @param message detailed error message
     */
    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}