package com.lulski.aries.util;

public enum ResponseStatus {
    SUCCESS("success"),
    FAILED("failed");

    private final String value;

    public String getValue() {
        return value;
    }

    ResponseStatus(String value) {
        this.value = value;
    }
}
