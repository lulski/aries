package com.lulski.aries.util;

public enum ResponseStatus {
    SUCCESS("success"),
    FAILED("failed");

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
