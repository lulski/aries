package com.lulski.aries.auth;

/**
 * returns authenticated username and it's roles
 *
 * @param username username
 * @param roles roles
 */
public record LoginResponseDto(
    String username, String firstName, String lastName, String[] roles) {}
