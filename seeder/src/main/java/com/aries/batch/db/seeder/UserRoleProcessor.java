package com.aries.batch.db.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRoleProcessor implements ItemProcessor<User, User> {
  private static final Logger log = LoggerFactory.getLogger(UserRoleProcessor.class);
  private final PasswordEncoder passwordEncoder;

  public UserRoleProcessor(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User process(@NonNull User item) throws Exception {
    log.info("Transforming item: {}, {}", item.username(), item.authorities());
    User transformedUser = new User(
        item.username(),
        passwordEncoder.encode(item.password()),
        item.firstName(),
        item.lastName(),
        item.email(),
        item.authorities());
    return transformedUser;
  }
}
