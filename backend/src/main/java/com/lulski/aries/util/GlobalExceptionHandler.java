package com.lulski.aries.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.lulski.aries.dto.InvalidRequestDto;
import com.lulski.aries.dto.ServerErrorResponse;
import com.lulski.aries.dto.ServerResponse;
import com.lulski.aries.user.UserNotFoundException;

import reactor.core.publisher.Mono;

/**
 * Controller exception handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * duplicate key handler.
     *
     * @param e       Exception thrown
     * @param request incoming server request
     * @return DTO
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<ServerResponse>> handleDuplicateKey(
        Exception e, ServerHttpRequest request) {
        LOGGER.error("Error happened on " + request.getMethod() + " " + request.getPath());
        e.printStackTrace();
        ServerErrorResponse errorResponse =
            new ServerErrorResponse(
                "Unique key error", e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());

        return Mono.just(ResponseEntity.unprocessableEntity().body(errorResponse));
    }

    /**
     * userNotFound handler.
     *
     * @param e       the thrown exception
     * @param request the submitted request
     * @return ResponseEntity detailing the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ServerResponse>> userNotFound(
        UserNotFoundException e, ServerHttpRequest request) {
        LOGGER.error("Can't find username " + request.getMethod() + " " + request.getPath());
        var username =
            request
                .getAttributes()
                .get("org.springframework.web.reactive.HandlerMapping.uriTemplateVariables")
                .toString();
        ServerErrorResponse errorResponse =
            new ServerErrorResponse(
                e.getMessage(), username + " doesn't exist", HttpStatus.NOT_FOUND.value());

        return Mono.just(ResponseEntity.unprocessableEntity().body(errorResponse));
    }

    /**
     * caught database unique key exception.
     *
     * @param e       the thrown exception
     * @param request the submitted request
     * @return ResponseEntity detailing the error
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<ServerResponse>> handleInvalidRequest(
        Exception e, ServerHttpRequest request) {
        LOGGER.error("Error happened on " + request.getMethod() + " " + request.getPath());
        e.printStackTrace();
        ServerErrorResponse errorResponse =
            new ServerErrorResponse(
                "Invalid request body",
                "username, firstname, and email are required",
                HttpStatus.UNPROCESSABLE_ENTITY.value());

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    /**
     * caught request missing fields.
     *
     * @param e       the thrown exception
     * @param request the submitted request
     * @return ResponseEntity detailing the error
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<InvalidRequestDto>> handleInvalidRequest2(
        Exception e, ServerHttpRequest request) {
        LOGGER.error("Error happened on " + request.getMethod() + " " + request.getPath());
        e.printStackTrace();
        InvalidRequestDto dto =
            new InvalidRequestDto(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), request.getId());
        return Mono.just(ResponseEntity.badRequest().body(dto));
    }
}
