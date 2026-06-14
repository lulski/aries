package com.lulski.aries.aws.s3;

public class PresignedUrlException extends RuntimeException {
    public PresignedUrlException(String message) {
        super(message);
    }

    public PresignedUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
