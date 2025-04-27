package com.lulski.aries.user;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class UserTestConfiguration {

    @Bean
    UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }

    @Bean
    UserController userController(UserRepository userRepository, UserService userService) {
        return new UserController(userRepository, userService);
    }
}
