package com.lulski.aries.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Test config to disable Spring security
 */
@TestConfiguration
@Profile("mock")
public class TestWebSecurityConfig {

    /**
     * override SpringSecurity SecurityWebFilterChain bean
     */
    @Bean
    @Primary
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
            .csrf(csrfSpec -> csrfSpec.disable())
            .authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll());

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails userDetails =
            User.withUsername("test").password(passwordEncoder.encode("test")).roles("USER").build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
