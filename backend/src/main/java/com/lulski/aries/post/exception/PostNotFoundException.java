package com.lulski.aries.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String id) {
        super("Can't find post with id/title: " + id);
    }
}
