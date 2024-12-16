package com.lulski.aries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class TestWebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .anyExchange()
                                .permitAll()
                );

        return httpSecurity.build();
    }
}
