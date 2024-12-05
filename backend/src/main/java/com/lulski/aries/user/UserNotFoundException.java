package com.lulski.aries.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User Not Found");
    }
}

