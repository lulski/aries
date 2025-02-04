package com.lulski.aries.dto;

/**
 * DTO for invalid request
 */
public record InvalidRequestDto(int httpCode, String message, String corrId) {

}
