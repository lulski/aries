package com.lulski.aries.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class PostRouter {

    @Autowired
    PostHandler postHandler;

    // public PostRouter(PostHandler postHandler) {
    // this.postHandler = postHandler;
    // }

    /**
     * Configures the routing for post-related HTTP endpoints.
     * <p>
     * This method sets up the following routes:
     * - POST /posts: Inserts a new post
     * - GET /posts: Lists all posts
     * - GET /posts/?title={title}: Finds posts by title
     * - GET /posts/?id={id}: Finds a post by its ID
     *
     * @param postHandler The handler that processes the requests for each route
     * @return A RouterFunction that maps incoming requests to their respective
     * handler methods
     */
    @Bean
    RouterFunction<ServerResponse> route(PostHandler postHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/posts"), postHandler::insertNew)
            .andRoute(RequestPredicates.GET("/posts")
                    .and(RequestPredicates.queryParam("title", this::isValidTitle)),
                postHandler::findByTitle)
            .andRoute(RequestPredicates.GET("/posts")
                    .and(RequestPredicates.queryParam("id", this::isValidId)),
                postHandler::findById)
            .andRoute(RequestPredicates.GET("/posts").and(req -> req.queryParams().isEmpty()),
                postHandler::listAll)
            .andRoute(RequestPredicates.GET("/posts"), this::handleInvalidQueryParams);
    }

    private boolean isValidTitle(String title) {
        return title!=null && !title.trim().isEmpty() && title.length() <= 100;
    }

    private boolean isValidId(String id) {
        return id!=null && id.matches("^[0-9a-fA-F]{24}$");
    }

    private Mono<ServerResponse> handleInvalidQueryParams(ServerRequest request) {
        return ServerResponse.badRequest().bodyValue("Invalid query parameters");
    }
}
