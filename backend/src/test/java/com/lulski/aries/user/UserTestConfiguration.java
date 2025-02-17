package com.lulski.aries.user;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class UserTestConfiguration {

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return new UserService(userRepository, passwordEncoder);
  }

  @Bean
  UserController userController(UserRepository userRepository, UserService userService) {
    return new UserController(userRepository, userService);
  }
}
