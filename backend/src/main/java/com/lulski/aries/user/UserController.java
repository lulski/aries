package com.lulski.aries.user;

import static com.lulski.aries.util.Constant.PATH_USER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lulski.aries.util.ResponseStatus;

import java.util.List;
import reactor.core.publisher.Mono;

/**
 * User controller reactive
 */
@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * main constructor
     *
     * @param userRepository
     * @param userService
     */
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * insert user into collection 'users'.
     *
     * @param
     * @return
     */
    @PostMapping(PATH_USER)
    public Mono<ResponseEntity<UserControllerResponseDto>> addUser(
        @RequestBody UserRequestDto userDto) {

        return userService
            .insertNew(userDto)
            .map(
                savedUser -> {
                    UserControllerResponseDto userControllerResponseDto =
                        new UserControllerResponseDto(
                            List.of(UserDto.fromUser(savedUser)), ResponseStatus.SUCCESS.getValue());
                    return ResponseEntity.ok().body((userControllerResponseDto));
                })
            .onErrorResume(
                e -> {
                    LOGGER.error(e.getMessage());
                    UserControllerResponseDto userControllerResponseDto =
                        new UserControllerResponseDto(List.of(), ResponseStatus.FAILED.getValue());
                    return Mono.just(
                        ResponseEntity.internalServerError().body(userControllerResponseDto));
                });
    }

    /**
     * return `user` based on the supplied `username`.
     *
     * @param username
     * @return
     */
    @GetMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<UserControllerResponseDto>> getUserByUsername(
        @PathVariable String username) {

        return userRepository
            .findTopByUsername(username)
            .map(
                foundUser ->
                    ResponseEntity.ok()
                        .body(
                            new UserControllerResponseDto(
                                List.of(UserDto.fromUser(foundUser)),
                                ResponseStatus.SUCCESS.getValue())))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * update `user` based on the supplied `username`
     *
     * @param username
     * @param userRequestDto
     * @return updated User information
     */
    @PatchMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<UserControllerResponseDto>> updateByUsername(
        @PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService
            .update(username, userRequestDto)
            .map(
                updatedUser ->
                    ResponseEntity.ok(
                        new UserControllerResponseDto(
                            List.of(UserDto.fromUser(updatedUser)), ResponseStatus.SUCCESS.getValue())))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .onErrorResume(
                e -> {
                    LOGGER.error(e.getMessage());
                    return Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                new UserControllerResponseDto(
                                    List.of(), ResponseStatus.FAILED.getValue())));
                });
    }

    /**
     * delete `user` based on the supplied username
     *
     * @param username
     * @return
     */
    @DeleteMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<UserControllerResponseDto>> deleteUserByUsername(
        @PathVariable String username) {

        return userRepository
            .deleteByUsername(username)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .body(
                            new UserControllerResponseDto(
                                List.of(), "user " + username + " successfully deleted"))))
            .onErrorResume(
                e ->
                    Mono.just(
                        ResponseEntity.internalServerError()
                            .body(
                                new UserControllerResponseDto(
                                    List.of(), "failed deleting user: " + username))));
    }

    /**
     * return all users (paginated)
     *
     * @return
     */
    @GetMapping(PATH_USER)
    public Mono<ResponseEntity<UserControllerResponseDto>> listAllUsers() {
        return userService
            .findAll()
            .map(UserDto::fromUser)
            .collectList()
            .map(
                dtoList ->
                    ResponseEntity.ok()
                        .body(
                            new UserControllerResponseDto(dtoList, ResponseStatus.SUCCESS.getValue())))
            .onErrorResume(
                e -> {
                    LOGGER.error(e.getMessage());
                    return Mono.just(
                        ResponseEntity.internalServerError()
                            .body(
                                new UserControllerResponseDto(
                                    List.of(), ResponseStatus.FAILED.getValue())));
                });
    }
}
