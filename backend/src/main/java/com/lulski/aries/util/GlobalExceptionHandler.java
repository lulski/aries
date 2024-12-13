package com.lulski.aries.util;

import com.lulski.aries.dto.ServerErrorResponse;
import com.lulski.aries.dto.ServerResponse;
import com.lulski.aries.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<ServerResponse>> handleDuplicateKey(Exception e, ServerHttpRequest request) {
        LOGGER.error("Error happened on " + request.getMethod() + " " + request.getPath());
        e.printStackTrace();
        ServerErrorResponse errorResponse = new ServerErrorResponse("Unique key error", e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value());

        return Mono.just(ResponseEntity.unprocessableEntity().body(errorResponse));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ServerResponse>> userNotFound(UserNotFoundException e, ServerHttpRequest request) {
        LOGGER.error("Can't find username " + request.getMethod() + " " + request.getPath());
        var username = request.getAttributes().get("org.springframework.web.reactive.HandlerMapping.uriTemplateVariables").toString();
        ServerErrorResponse errorResponse = new ServerErrorResponse(e.getMessage(), username + " doesn't exist", HttpStatus.NOT_FOUND.value());

        return Mono.just(ResponseEntity.unprocessableEntity().body(errorResponse));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<ServerResponse>> handleInvalidRequest(Exception e, ServerHttpRequest request) {
        LOGGER.error("Error happened on " + request.getMethod() + " " + request.getPath());
        e.printStackTrace();
        ServerErrorResponse errorResponse = new ServerErrorResponse("Invalid request body", "username, firstname, and email are required",
                HttpStatus.UNPROCESSABLE_ENTITY.value());

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }
}
