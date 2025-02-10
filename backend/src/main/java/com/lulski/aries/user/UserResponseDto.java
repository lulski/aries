package com.lulski.aries.user;


import java.util.Set;

public record UserResponseDto(
    String username, String id, Set<String> authorities) {}
