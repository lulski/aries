package com.lulski.aries.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lulski.aries.post.exception.BadRequestException;
import com.lulski.aries.post.exception.DatabaseAccessException;
import com.lulski.aries.post.exception.NetworkTimeoutException;
import com.lulski.aries.post.exception.PostNotFoundException;
import com.lulski.aries.user.User;
import com.lulski.aries.util.ResponseStatus;

import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

@Component
public class PostHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostHandler.class);
    @Autowired
    PostService postService;

    public Mono<ServerResponse> listAll(ServerRequest serverRequest) {
        return postService.listAll().collectList().flatMap(posts -> {
            PostResponseDto postResponseDto = new PostResponseDto(PostResponseDto.fromPosts(posts),
                    ResponseStatus.SUCCESS.getValue());
            return ServerResponse.ok().body(BodyInserters.fromValue(postResponseDto));
        });
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.queryParam("id")
                .orElseThrow(() -> new BadRequestException("id query parameter is missing"));

        return postService.getById(id)
                .flatMap(post -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(PostResponseDto.fromPost(post))))
                .onErrorResume(error -> {
                    return this.handleError(error);
                });
    }

    public Mono<ServerResponse> findByTitle(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String title = serverRequest.queryParam("title")
                    .orElseThrow(() -> new BadRequestException("title query parameter is missing"));

            return postService.getByTitle(title)
                    .flatMap(post -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(PostResponseDto.fromPost(post))))
                    .onErrorResume(this::handleError);
        });
    }

    public Mono<ServerResponse> insertNew(ServerRequest serverRequest) {

        Mono<Authentication> principalMono = serverRequest.principal().cast(Authentication.class)
                .filter(authentication -> authentication.isAuthenticated()
                        && authentication.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().contains("USER")))
                .switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException(
                        "user not authenticated")));

        return Mono.zip(principalMono, serverRequest.bodyToMono(PostRequestDto.class)).flatMap(tuple -> {
            Authentication authentication = tuple.getT1();
            PostRequestDto dto = tuple.getT2();

            LOGGER.info(">>> user " + authentication.getPrincipal() + " is inserting new post "
                    + dto.title());
            return postService.insertNew(dto, (User) authentication.getPrincipal())
                    .flatMap(result -> ServerResponse.ok()
                            .body(BodyInserters.fromValue(new PostResponseDto(
                                    List.of(PostResponseDto.fromPost(result)),
                                    ResponseStatus.SUCCESS.getValue()))));
        }).onErrorResume(e -> {
            return handleError(e);
        });
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        LOGGER.error(error.getMessage());

        return Mono.just(
                switch (error) {
                    case PostNotFoundException postNotFoundException -> ServerResponse.status(HttpStatus.NOT_FOUND)
                            .bodyValue(Map.of("error", error.getMessage()));
                    case DatabaseAccessException databaseAccessException ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue(Map.of("error",
                                        "A database error occurred."));
                    case NetworkTimeoutException networkTimeoutException ->
                        ServerResponse.status(HttpStatus.GATEWAY_TIMEOUT)
                                .bodyValue(Map.of("error",
                                        "The request timed out. Please try again later."));
                    default -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(Map.of("error", "An unexpected error occurred."));
                }).flatMap(response -> response);

    }
}