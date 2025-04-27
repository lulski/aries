package com.lulski.aries.user;

import java.util.Set;

public record UserRequestDto(
    String username,
    String password,
    Set<String> authorities,
    String firstname,
    String lastname,
    String email) {
}
