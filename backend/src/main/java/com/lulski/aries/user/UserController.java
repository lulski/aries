package com.lulski.aries.user;

import com.lulski.aries.dto.ErrorResponse;
import com.lulski.aries.dto.ServerResponse;
import com.lulski.aries.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.lulski.aries.util.Constant.API_V1;

/**
 *
 */
@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final static String CONTROLLER_PATH = API_V1 + "/user";
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
    @PostMapping(CONTROLLER_PATH)
    public Mono<ResponseEntity<ServerResponse>> addUser(@RequestBody User user) {
        printLastLineStackTrace("POST " + CONTROLLER_PATH);

        return userRepository.save(user).map(savedUser -> ResponseEntity.accepted()
                        .body(new ServerResponse(user)))
                .onErrorResume(e -> {
                    ErrorResponse errorResponse = new ErrorResponse(e.getClass().getName(), e.getMessage(),
                            HttpStatus.UNPROCESSABLE_ENTITY.value());
                    return Mono.just(ResponseEntity.unprocessableEntity().body(new ServerResponse(errorResponse)));
                });
    }

    @GetMapping(CONTROLLER_PATH + "/{username}")
    public Mono<ResponseEntity<User>> getUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("GET " + CONTROLLER_PATH + "/" + username);

        return userRepository.findTopByUsername(username).map(foundUser -> ResponseEntity.ok().body(foundUser)).switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping(CONTROLLER_PATH + "/{username}")
    public Mono<ResponseEntity<User>> updateByUsername(@PathVariable String username, @RequestBody User user) {
        printLastLineStackTrace("PATCH " + CONTROLLER_PATH + "/" + username);

        return userRepository.findTopByUsername(username).flatMap(foundUser -> {
            foundUser.setFirstName(user.getFirstName());
            foundUser.setLastName(user.getLastName());
            foundUser.setEmail(user.getEmail());

            return userRepository.save(foundUser).map(updatedUser -> ResponseEntity.ok().body(updatedUser));
        }).switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));

    }

    @DeleteMapping(CONTROLLER_PATH + "/{username}")
    public Mono<ResponseEntity<Object>> deleteUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("DEL " + CONTROLLER_PATH + "/" + username);

        return userRepository.deleteByUsername(username).then(Mono.just(ResponseEntity.noContent().build())).onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping(API_V1 + "/users")
    public Mono<Page<User>> listAllUsers(@RequestParam int page, @RequestParam int size) {
        printLastLineStackTrace("GET " + CONTROLLER_PATH + "s" + "&page=" + page + "&size=" + size);
        int skipCount = (page - 1) * size; // Calculate the number of items to skip

        var paginatedUsers = userRepository.findAll().skip(skipCount).take(size);

        //debug only
        //        paginatedUsers.subscribe(user -> System.out.println("Received user: " + user.getUsername()),
        //                error -> System.out.println("Error: " + error),
        //                () -> System.out.println("All users received."));

        var totalCount = userRepository.count();

        //debug only
        //        totalCount.subscribe(value -> LOGGER.info("totalCount: " + value));

        return Mono.zip(totalCount, paginatedUsers.collectList()).map(tuple -> new Page<>(tuple.getT1(), tuple.getT2(), page, size));
    }

}
