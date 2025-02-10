package com.lulski.aries.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

/** Confing to use real mongodb instance for TDD */
@Configuration
@Profile("!mock")
public class TestDbMongoConfig extends AbstractReactiveMongoConfiguration {

  @Override
  protected String getDatabaseName() {
    return "aries_db_test";
  }
}
