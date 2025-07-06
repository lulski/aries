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

    public Mono<ServerResponse> listAllPaginated(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        //adjust frontend supplied page value (eg: posts?page=1&size=1)
        // as Spring Data uses 0-based indexing.
        int adjustedPage = Math.max(0, page -1 );

        Mono<List<Post>> postsMono = postService.listAll(page, size).collectList();
        Mono<Long> totalMono = postService.countAllPosts();

        return Mono.zip(postsMono, totalMono)
                .flatMap(tuple -> {
                    List<Post> posts = tuple.getT1();
                    long total = tuple.getT2();

                    PostResponseDto responseDto = new PostResponseDto(
                            PostResponseDto.fromPosts(posts),
                            ResponseStatus.SUCCESS.getValue(),
                            adjustedPage,
                            size,
                            total
                    );

                    return ServerResponse.ok().body(BodyInserters.fromValue(responseDto));
                });
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.queryParam("id")
            .orElseThrow(() -> new BadRequestException("id query parameter is missing"));

        return postService.getById(id)
            .flatMap(post -> ServerResponse.ok()
                .body(BodyInserters.fromValue(PostResponseDto.fromPost(post))))
            .onErrorResume(this::handleError);
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
                .flatMap(post -> {
                    PostResponseDto postResponseDto = new PostResponseDto(
                            List.of(PostResponseDto.fromPost(post)),
                            ResponseStatus.SUCCESS.getValue(),
                            0,
                            1,
                            1
                            );
                    return ServerResponse.ok().body(BodyInserters.fromValue(postResponseDto));
                });
        }).onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        LOGGER.error(error.getCause().getMessage());

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