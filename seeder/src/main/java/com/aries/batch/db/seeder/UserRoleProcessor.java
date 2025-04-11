package com.aries.batch.db.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class UserRoleProcessor implements ItemProcessor<User, User> {
  private static final Logger log = LoggerFactory.getLogger(UserRoleProcessor.class);

  @Override
  public User process(User item) throws Exception {
    log.info("Processing user: {}, {}", item.username(), item.password());
    return item;
  }
}
