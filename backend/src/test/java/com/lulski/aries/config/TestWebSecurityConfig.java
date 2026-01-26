package com.lulski.aries.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.mongodb.reactivestreams.client.MongoClient;

/**
 * Test configuration for Spring Security in mock profile.
 * 
 * This configuration is activated when the "mock" profile is active and provides
 * a permissive security setup suitable for integration testing:
 * - CSRF protection is disabled
 * - All exchanges are permitted without authentication
 * - In-memory user details manager is configured with a default test user
 * 
 * Usage: Activate with @ActiveProfiles("mock") in test classes.
 */
@TestConfiguration
@Profile("mock")
public class TestWebSecurityConfig {

    /**
     * Provides a permissive security configuration for testing.
     * 
     * Disables CSRF protection and allows all exchanges without authentication.
     * This is suitable for integration tests where security enforcement is not the focus.
     * 
     * @param httpSecurity the reactive HTTP security configuration builder
     * @return configured SecurityWebFilterChain
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

    /**
     * Provides an in-memory user details manager with a default test user.
     * 
     * Test user credentials:
     * - Username: test
     * - Password: test
     * - Role: USER
     * 
     * @param passwordEncoder the password encoder for encoding credentials
     * @return configured InMemoryUserDetailsManager
     */
    @Bean
    @Primary
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("test")
                .password(passwordEncoder.encode("test"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    /**
     * Provides a delegating password encoder for use in tests.
     * 
     * Uses Spring's factory to create an encoder that supports multiple algorithms.
     * 
     * @return configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Provides a mock UserDetailsPasswordService for tests.
     * 
     * This bean satisfies dependencies on UserDetailsPasswordService without
     * requiring actual password update functionality in tests.
     * 
     * @return mocked UserDetailsPasswordService
     */
    @Bean
    @Primary
    public UserDetailsPasswordService userDetailsPasswordService() {
        return Mockito.mock(UserDetailsPasswordService.class);
    }

    /**
     * Provides a mock MongoClient for tests.
     * 
     * This prevents Spring from trying to connect to MongoDB during test startup.
     * 
     * @return mocked MongoClient
     */
    @Bean
    @Primary
    public MongoClient mongoClient() {
        return Mockito.mock(MongoClient.class);
    }
}
