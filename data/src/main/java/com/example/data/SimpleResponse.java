package com.example.data;

/**
 * The response for a call for a single object to the LCBO API.
 */
public class SimpleResponse<T> {

    private final int status;
    private final String message;
    private final T result;
    private final String error;

    public SimpleResponse(int status, String message, T result, String error) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
