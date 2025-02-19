package com.lulski.aries.user;

import java.util.Set;

public record UserDto(
    String username, String firstname, String lastname, String email, Set<String> authorityNames) {
  public static UserDto fromUser(User user) {
    return new UserDto(
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getAuthoritiesNames());
  }
}
