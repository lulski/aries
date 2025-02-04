package com.lulski.aries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/** Config for security. */
@Configuration
@EnableWebFluxSecurity
@Profile("!mock")
public class WebSecurityConfig {

  /**
   * setup Spring security USER cred.
   *
   * @return userDetailsService
   */
  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user =
        User.withDefaultPasswordEncoder().username("fanta").password("fanta").roles("USER").build();

    return new MapReactiveUserDetailsService(user);
  }

  /**
   * testing some security config.
   *
   * @param serverHttpSecurity lambda
   * @return securityWebFilterChain
   */
  @Bean
  @Profile("!mock")
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
    serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

    serverHttpSecurity.authorizeExchange(e -> e.pathMatchers("/actuator/**").permitAll());

    serverHttpSecurity.authorizeExchange(
        e -> e.pathMatchers(HttpMethod.GET, "/users/**").permitAll());
    serverHttpSecurity
        .authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/users/**").authenticated())
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());

    return serverHttpSecurity.build();
  }
}
