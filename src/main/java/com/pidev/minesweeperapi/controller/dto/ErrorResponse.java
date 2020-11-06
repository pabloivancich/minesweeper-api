package com.pidev.minesweeperapi.controller.dto;

/**
 * Represents an error response from the API.
 */
public class ErrorResponse {

    private int status;
    private String type;
    private String message;

    /**
     * Default constructor. For Jackson.
     */
    public ErrorResponse() {

    }

    public ErrorResponse(int status, String type, String message) {
        this.status = status;
        this.type = type;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
