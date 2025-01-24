package com.lulski.aries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    /*
     * @Bean
     * public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity
     * httpSecurity) {
     * httpSecurity.authorizeExchange(exchanges ->
     * exchanges.anyExchange().authenticated())
     * .httpBasic(Customizer.withDefaults())
     * .formLogin(Customizer.withDefaults());
     * 
     * 
     * return httpSecurity.build();
     * }
     */

    @Bean
    @Profile("dev")
    public SecurityWebFilterChain devSecurityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .anyExchange()
                        .permitAll());

        return httpSecurity.build();
    }

}
