package com.lulski.aries.dto;

import com.lulski.aries.user.User;

public class ServerResponse {

    private User item;
    private ErrorResponse error;

    public ServerResponse() {
    }

    public ServerResponse(User item) {
        this.item = item;
    }

    public ServerResponse(ErrorResponse errorResponse) {
        this.error = errorResponse;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public User getItem() {
        return item;
    }

    public void setItem(User item) {
        this.item = item;
    }
}
