package com.lulski.aries.post.exception;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(Throwable cause) {
        super("Database error", cause);
    }
}
