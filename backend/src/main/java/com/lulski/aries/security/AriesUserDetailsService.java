package com.lulski.aries.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class AriesUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public AriesUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository
            .findTopByUsername(username)
            .map(
                user -> {
                    User.UserBuilder userBuilder = new User.UserBuilder();

                    userBuilder
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .firstname(user.getFirstName())
                        .lastname(user.getLastName())
                        .authorities(user.getAuthoritiesNames());

                    return userBuilder.build();
                });
    }
}
