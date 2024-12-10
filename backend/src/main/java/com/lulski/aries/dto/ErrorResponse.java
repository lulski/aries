package com.lulski.aries.dto;

public class ErrorResponse extends ServerResponse {
    private String message;
    private String details;
    private int errorCode;

    public ErrorResponse(String message, String details, int errorCode) {
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
