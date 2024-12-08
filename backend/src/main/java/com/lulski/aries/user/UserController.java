package com.lulski.aries.user;

import com.lulski.aries.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 *
 */
@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final static String CONTROLLER_PATH = Constant.API_V1 + "/user";
    private final UserRepository userRepository;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static void printLastLineStackTrace(String context) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        LOGGER.info("Stack trace's last line: " + stackTrace[stackTrace.length - 1].toString() + " from " + context);
    }

    /**
     * insert user into collection 'users'
     *
     * @param user
     * @return
     */
    @PostMapping(CONTROLLER_PATH)
    public Mono<ResponseEntity<User>> addUser(@RequestBody User user) {
        printLastLineStackTrace("POST" + CONTROLLER_PATH);
        return userRepository.save(user) // Save the user to the database
                .map(savedUser -> ResponseEntity.accepted().body(savedUser)) // Map the result to ResponseEntity
                .onErrorResume(e -> {
                    e.printStackTrace(); // Log the error
                    return Mono.just(ResponseEntity.internalServerError()
                            .body(null)); // Return an error response
                });
    }

    @GetMapping(CONTROLLER_PATH + "/{username}")
    public Mono<ResponseEntity<User>> getUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("GET" + CONTROLLER_PATH);

        return userRepository.findByUsername(username)
                .map(foundUser -> ResponseEntity.ok().body(foundUser))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping(CONTROLLER_PATH + "/{username}")
    public Mono<ResponseEntity<Object>> deleteUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("DEL" + CONTROLLER_PATH);

        return userRepository.deleteByUsername(username)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));

    }
}
