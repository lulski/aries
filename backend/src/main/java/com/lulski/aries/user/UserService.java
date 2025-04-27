package com.lulski.aries.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User service.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Update User with supplied username with attributes from UserRequestDto.
     *
     * @param username       username of an existing User
     * @param userRequestDto User fields that will be updated
     * @return User object wrapped in Mono
     */
    public Mono<User> update(String username, UserRequestDto userRequestDto) {
        return userRepository
            .findTopByUsername(username)
            .flatMap(
                existingUser -> {
                    if (StringUtils.hasText(userRequestDto.firstname())) {
                        existingUser.setFirstName(userRequestDto.firstname());
                    }
                    if (StringUtils.hasText(userRequestDto.lastname())) {
                        existingUser.setLastName(userRequestDto.lastname());
                    }
                    if (StringUtils.hasText(userRequestDto.email())) {
                        existingUser.setEmail(userRequestDto.email());
                    }
                    if (StringUtils.hasText(userRequestDto.password())) {
                        existingUser.setPassword(passwordEncoder.encode(userRequestDto.password()));
                    }
                    return userRepository.save(existingUser);
                })
            .switchIfEmpty(Mono.error(new UserNotFoundException("User not found: " + username)));
    }

    public Mono<User> insertNew(UserRequestDto userRequestDto) {
        User toSave = new User();
        toSave.setUsername(userRequestDto.username());
        toSave.setPassword(passwordEncoder.encode(userRequestDto.password()));
        toSave.setAuthorities(userRequestDto.authorities());
        toSave.setFirstName(userRequestDto.firstname());
        toSave.setLastName(userRequestDto.lastname());
        toSave.setEmail(userRequestDto.email());

        return userRepository.save(toSave);
    }

    /**
     * fetch all User and strip out its password.
     */
    public Flux<User> findAll() {
        return this.userRepository
            .findAll()
            .map(
                user -> {
                    user.setPassword("****");
                    return user;
                });
    }

    /**
     * return one User based on supplied username (case insensitive)
     *
     * @param username
     * @return User instance
     */
    public Mono<User> findByUsernameAndPassword(String username, String password) {
        return this.userRepository
            .findTopByUsername(username.toLowerCase())
            .flatMap(
                user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(user);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
