package com.lulski.aries.post;

import java.util.List;
import java.util.Map;

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

import reactor.core.publisher.Mono;

@Component
public class PostHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(PostHandler.class);
        @Autowired
        PostService postService;

        public Mono<ServerResponse> listAllPaginated(ServerRequest serverRequest) {
                int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
                int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
                // adjust frontend supplied page value (eg: posts?page=1&size=1)
                // as Spring Data uses 0-based indexing.
                int adjustedPage = Math.max(0, page - 1);

                Mono<List<Post>> postsMono = postService.listAll(adjustedPage, size).collectList();
                Mono<Long> totalMono = postService.countAllPosts();

                return Mono.zip(postsMono, totalMono)
                                .flatMap(tuple -> {
                                        List<Post> posts = tuple.getT1();
                                        long total = tuple.getT2();

                                        PostResponseDto responseDto = new PostResponseDto(
                                                        PostResponseDto.fromPosts(posts),
                                                        ResponseStatus.SUCCESS.getValue(),
                                                        page,
                                                        size,
                                                        total);

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

                        // if the title contains dash ex: "elevent-post", this is how MFE will call it
                        // remove the dash
                        String titleWithoutDashes = PostUtil.sanitizeTitleForURL_reverse(title);

                        return postService.getByTitle(titleWithoutDashes)
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

                        LOGGER.info(">>> user `{}` is inserting new post {}", authentication.getName(), dto.title());

                        return postService.insertNew(dto, (User) authentication.getPrincipal())
                                        .flatMap(post -> {
                                                PostResponseDto postResponseDto = new PostResponseDto(
                                                                List.of(PostResponseDto.fromPost(post)),
                                                                ResponseStatus.SUCCESS.getValue(),
                                                                0,
                                                                1,
                                                                1);
                                                return ServerResponse.ok()
                                                                .body(BodyInserters.fromValue(postResponseDto));
                                        });
                }).onErrorResume(this::handleError);
        }

        /**
         * Updates an existing post with new data provided in the request body.
         * <p>
         * This method requires the user to be authenticated and have the "USER"
         * authority.
         * It extracts the authenticated principal and the {@link PostRequestDto} from
         * the request,
         * then delegates the update operation to the {@link PostService}. On success,
         * it returns
         * a {@link ServerResponse} containing the updated post data wrapped in a
         * {@link PostResponseDto}.
         * </p>
         * 
         * @param serverRequest the {@link ServerRequest} containing authentication and
         *                      post update data
         * @return a {@link Mono} emitting the {@link ServerResponse} with the updated
         *         post or an error response
         */
        public Mono<ServerResponse> update(ServerRequest serverRequest) {
                Mono<Authentication> principalMono = serverRequest.principal().cast(Authentication.class)
                                .filter(authentication -> authentication.isAuthenticated()
                                                && authentication.getAuthorities().stream()
                                                                .anyMatch(auth -> auth.getAuthority().contains("USER")))
                                .switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException(
                                                "user not authenticated")));

                return Mono.zip(principalMono, serverRequest.bodyToMono(PostRequestDto.class))
                                .flatMap(tuple -> {
                                        Authentication authentication = tuple.getT1();
                                        PostRequestDto dto = tuple.getT2();

                                        return postService.updatePost(dto, (User) authentication.getPrincipal());
                                })
                                .flatMap(updatedPost -> {
                                        PostResponseDto postResponseDto = new PostResponseDto(
                                                        List.of(PostResponseDto.fromPost(updatedPost)),
                                                        ResponseStatus.SUCCESS.getValue(),
                                                        0, 1, 1);

                                        return ServerResponse.ok().body(BodyInserters.fromValue(postResponseDto));
                                });

        }

        public Mono<ServerResponse> handleError(Throwable error) {
                return Mono.deferContextual(ctxView -> {
                        // Extract corrId from Reactor Context
                        // String corrId = ctxView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY,
                        // "N/A");

                        // Build the error response depending on exception type
                        Mono<ServerResponse> responseMono = switch (error) {
                                case BadRequestException ex -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                                .bodyValue(Map.of("error", ex.getMessage()));

                                case PostNotFoundException ex ->
                                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                                        .bodyValue(Map.of(
                                                                        "error", ex.getMessage()));
                                case DatabaseAccessException ex ->
                                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .bodyValue(Map.of(
                                                                        "error", "A database error occurred."));
                                case NetworkTimeoutException ex ->
                                        ServerResponse.status(HttpStatus.GATEWAY_TIMEOUT)
                                                        .bodyValue(Map.of(
                                                                        "error",
                                                                        "The request timed out. Please try again later."));
                                default ->
                                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .bodyValue(Map.of(
                                                                        "error", "An unexpected error occurred."));
                        };

                        return responseMono;
                });
        }
}
