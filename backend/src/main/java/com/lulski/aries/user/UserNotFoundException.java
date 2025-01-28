package com.lulski.aries.user;

/**
 * Exception class for failed User entity lookup
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User Not Found");
    }
}
