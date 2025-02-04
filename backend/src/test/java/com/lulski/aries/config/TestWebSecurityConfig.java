package com.lulski.aries.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Test config to disable Spring security
 */
@TestConfiguration
public class TestWebSecurityConfig {

    /**
     * override SpringSecurity SecurityWebFilterChain bean
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .anyExchange()
                        .permitAll());

        return httpSecurity.build();
    }
}
