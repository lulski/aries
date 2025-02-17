package com.lulski.aries.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/** User service */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * update User object.
   *
   * @param username
   * @param userRequestDto
   * @return Mono user
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

  /**
   * set originalUser fields with the values from userUpdate
   *
   * @param originalUser
   * @param userUpdate
   */
  public void prepareUpdate(User originalUser, User userUpdate) {
    userUpdate.setId(originalUser.getId());

    if (userUpdate.getFirstName() == null) {
      userUpdate.setFirstName(originalUser.getFirstName());
    }

    if (userUpdate.getLastName() == null) {
      userUpdate.setLastName(originalUser.getLastName());
    }

    if (userUpdate.getEmail() == null) {
      userUpdate.setEmail(originalUser.getEmail());
    }
  }

  public Mono<User> insertNew(User user) {
    User toSave = new User();
    toSave.setUsername(user.getUsername());
    toSave.setPassword(passwordEncoder.encode(user.getPassword()));
    toSave.setAuthorities(user.getAuthoritiesNames());
    toSave.setFirstName(user.getFirstName());
    toSave.setLastName(user.getLastName());
    toSave.setEmail(user.getEmail());

    return userRepository.save(toSave);
  }
}
