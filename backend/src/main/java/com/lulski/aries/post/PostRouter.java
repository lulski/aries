package com.lulski.aries.post;

import com.lulski.aries.user.User;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PostRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PostRouter.class);

  @Bean
  RouterFunction<ServerResponse> route(PostHandler postHandler) {
    return RouterFunctions.route(RequestPredicates.GET("/posts"), postHandler::helloWorld)
        .andRoute(RequestPredicates.POST("/posts"), postHandler::insertNew);
  }
}

@Component
class PostHandler {

  @Autowired PostService postService;

  private static final Logger LOGGER = LoggerFactory.getLogger(PostHandler.class);

  public Mono<ServerResponse> helloWorld(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            BodyInserters.fromValue(
                new PostResponseDto(200, "kocu", "kocu", LocalDateTime.now().toString())));
  }

  public Mono<ServerResponse> insertNew(ServerRequest serverRequest) {

    Mono<Authentication> principalMono =
        serverRequest
            .principal()
            .cast(Authentication.class)
            .filter(
                authentication ->
                    authentication.isAuthenticated()
                        && authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().contains("USER")))
            .switchIfEmpty(
                Mono.error(
                    new AuthenticationCredentialsNotFoundException("user not authenticated")));

    return Mono.zip(principalMono, serverRequest.bodyToMono(PostRequestDto.class))
        .flatMap(
            tuple -> {
              Authentication authentication = tuple.getT1();
              PostRequestDto dto = tuple.getT2();

              return postService
                  .insertNew(dto, (User) authentication.getPrincipal())
                  .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
            });
  }
}
