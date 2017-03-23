package com.example.data;

import java.util.List;

/**
 * The response for a call for a multi-valued result to the LCBO API.
 */
public class ListResponse<T> {

    private final int status;
    private final String message;
    private final Pager pager;
    private final List<T> results;
    private final String error;

    public ListResponse(int status, String message, Pager pager, List<T> results, String error) {
        this.status = status;
        this.message = message;
        this.pager = pager;
        this.results = results;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Pager getPager() {
        return pager;
    }

    public List<T> getResults() {
        return results;
    }

    public String getError() {
        return error;
    }
}
