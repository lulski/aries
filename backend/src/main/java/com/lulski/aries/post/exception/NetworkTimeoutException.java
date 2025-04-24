package com.lulski.aries.post.exception;

public class NetworkTimeoutException extends RuntimeException{
    public NetworkTimeoutException(Throwable cause) {
        super("Network timeout", cause);
    }
}
