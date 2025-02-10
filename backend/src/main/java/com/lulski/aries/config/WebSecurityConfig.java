package com.lulski.aries.config;

import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** Config for security. */
@Configuration
@EnableWebFluxSecurity
@Profile(value = {"!test"})
public class WebSecurityConfig {

  /**
   * setup Spring security USER cred.
   *
   * @return userDetailsService
   */
  // todo refactor with credential from database
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
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
    serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

    serverHttpSecurity.httpBasic(Customizer.withDefaults());

    serverHttpSecurity.authorizeExchange(e -> e.pathMatchers("/actuator/**").permitAll());

    serverHttpSecurity
        .authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/users/**").authenticated())
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());

    return serverHttpSecurity.build();
  }

  @Component
  // TODO authenticate from database
  class CustomAuthenticationProvider implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication)
        throws AuthenticationException {
      String username = authentication.getName();
      String password = String.valueOf(authentication.getCredentials());

      System.out.println(">>>");

      Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

      return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }
  }
}
