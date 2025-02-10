package com.lulski.aries.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/** Test config to disable Spring security */
@TestConfiguration
@Profile("test")
public class TestWebSecurityConfig {

  /** override SpringSecurity SecurityWebFilterChain bean */
  @Bean
  @Primary
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
    httpSecurity
        .csrf(csrfSpec -> csrfSpec.disable())
        .authorizeExchange(
            authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll());

    return httpSecurity.build();
  }
}
