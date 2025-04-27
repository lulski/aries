package com.lulski.aries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.lulski.aries.security.AriesUserDetailsService;

/**
 * Config for security.
 */
@Configuration
@EnableWebFluxSecurity
@Profile("!mock")
public class WebSecurityConfig {

    private final AriesUserDetailsService ariesUserDetailsService;

    public WebSecurityConfig(AriesUserDetailsService ariesUserDetailsService) {
        this.ariesUserDetailsService = ariesUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * testing some security config.
     *
     * @param serverHttpSecurity lambda
     * @return securityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

        serverHttpSecurity.httpBasic(Customizer.withDefaults());

        serverHttpSecurity.authorizeExchange(e -> e.pathMatchers("/actuator/**").permitAll());

        serverHttpSecurity
            .authorizeExchange(
                e ->
                    e.pathMatchers("/users/**")
                        .hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/posts/**")
                        .permitAll()
                        .pathMatchers(HttpMethod.POST, "/posts/**")
                        .hasAuthority("USER")
                        .anyExchange()
                        .authenticated())
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());

        return serverHttpSecurity.build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(ariesUserDetailsService);
    }
}
