package com.lulski.aries.post;

import com.lulski.aries.user.User;
import com.lulski.aries.util.ResponseStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
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
    return RouterFunctions.route(RequestPredicates.POST("/posts"), postHandler::insertNew)
        .andRoute(RequestPredicates.GET("/posts"), postHandler::listAll);
  }
}

@Component
class PostHandler {

  @Autowired PostService postService;

  private static final Logger LOGGER = LoggerFactory.getLogger(PostHandler.class);

  public Mono<ServerResponse> listAll(ServerRequest serverRequest) {
    return postService
        .listAll()
        .collectList()
        .flatMap(
            posts -> {
              PostResponseDto postResponseDto =
                  new PostResponseDto(
                      PostResponseDto.fromPosts(posts), ResponseStatus.SUCCESS.getValue());
              return ServerResponse.ok().body(BodyInserters.fromValue(postResponseDto));
            });
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
                  .flatMap(
                      result ->
                          ServerResponse.ok()
                              .body(
                                  BodyInserters.fromValue(
                                      new PostResponseDto(
                                          List.of(PostResponseDto.fromPost(result)),
                                          ResponseStatus.SUCCESS.getValue()))));
            })
        .onErrorResume(
            e -> {
              LOGGER.error(e.getMessage());
              return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            });
  }
}
