package com.lulski.aries.user;

import com.lulski.aries.dto.ServerResponse;
import com.lulski.aries.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.lulski.aries.util.Constant.*;

/**
 *
 */
@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
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
    @PostMapping(PATH_USER)
    public Mono<ResponseEntity<ServerResponse>> addUser(@RequestBody User user) {
        printLastLineStackTrace("POST " + PATH_USER);

        return userRepository.save(user)
                .map(savedUser -> ResponseEntity.accepted().body(new ServerResponse(user)));
    }

    @GetMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<ServerResponse>> getUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("GET " + PATH_USER + "/" + username);

        return userRepository.findTopByUsername(username)
                .map(
                        foundUser -> ResponseEntity.ok().body(new ServerResponse(foundUser)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<User>> updateByUsername(@PathVariable String username, @RequestBody User user) {
        printLastLineStackTrace("PATCH " + PATH_USER + "/" + username);
        user.setUsername(username);

        return userService.update(user)
                .then(Mono.just(ResponseEntity.accepted().body(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));

    }

    @DeleteMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<Object>> deleteUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("DEL " + PATH_USER + "/" + username);

        return userRepository.deleteByUsername(username)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping(PATH_USER)
    public Mono<Page<User>> listAllUsers(@RequestParam int page, @RequestParam int size) {
        printLastLineStackTrace("GET " + PATH_USER + "s" + "&page=" + page + "&size=" + size);
        int skipCount = (page - 1) * size; // Calculate the number of items to skip

        var paginatedUsers = userRepository.findAll().skip(skipCount).take(size);

        var totalCount = userRepository.count();

        return Mono.zip(totalCount, paginatedUsers.collectList())
                .map(tuple -> new Page<>(tuple.getT1(), tuple.getT2(), page, size));
    }

}
