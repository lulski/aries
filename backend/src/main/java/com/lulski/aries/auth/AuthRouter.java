package com.lulski.aries.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lulski.aries.user.UserService;

import reactor.core.publisher.Mono;

@Component
public class AuthRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRouter.class);

    @Bean
    RouterFunction<ServerResponse> authRoute(AuthHandler authHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/auth/login"), authHandler::login);
    }
}

@Component
class AuthHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);
    @Autowired
    UserService userService;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(LoginRequestDto.class)
            .flatMap(
                loginRequestDto ->
                    userService.findByUsernameAndPassword(
                        loginRequestDto.username(), loginRequestDto.password()))
            .flatMap(
                user ->
                    ServerResponse.ok()
                        .body(
                            BodyInserters.fromValue(
                                new LoginResponseDto(
                                    user.getUsername(),
                                    user.getFirstName(),
                                    user.getLastName(),
                                    user.getAuthoritiesNames().toArray(new String[0])))))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
