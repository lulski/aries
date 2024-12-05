package com.lulski.aries.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 *
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static void printLastLineStackTrace(String context) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        LOGGER.info("Stack trace's last line: " + stackTrace[stackTrace.length - 1].toString() + " from " + context);
    }

    /**
     * insert user into collection 'users'
     * @param user
     * @return
     */
    @PostMapping("/api/v1/user")
    public Mono<User> addUser(@RequestBody User user) {
        printLastLineStackTrace("POST /api/v1/user");
        return userRepository.save(user);
    }

    @GetMapping("/api/v1/user/{username}")
    public Mono<User> getUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("GET /api/v1/user/" + username);

        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }
}
