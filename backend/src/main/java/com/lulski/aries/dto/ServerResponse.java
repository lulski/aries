package com.lulski.aries.dto;

import com.lulski.aries.user.User;

public class ServerResponse {

    private User item;
    private ServerErrorResponse error;

    public ServerResponse() {
    }

    public ServerResponse(User item) {
        this.item = item;
    }

    public ServerResponse(ServerErrorResponse errorResponse) {
        this.error = errorResponse;
    }

    public ServerErrorResponse getError() {
        return error;
    }

    public void setError(ServerErrorResponse error) {
        this.error = error;
    }

    public User getItem() {
        return item;
    }

    public void setItem(User item) {
        this.item = item;
    }
}
