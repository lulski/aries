package com.lulski.aries.user;

import static com.lulski.aries.util.Constant.PATH_USER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lulski.aries.dto.ServerResponse;
import com.lulski.aries.util.Page;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * User controller reactive
 */
@RestController
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
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
    public Mono<ResponseEntity<ServerResponse>> addUser(@Valid @RequestBody User user) {
        printLastLineStackTrace("POST " + PATH_USER);

        return userRepository.save(user)
                .map(savedUser -> ResponseEntity.accepted().body(new ServerResponse(savedUser)));
    }

    /**
     * return `user` based on the supplied `username`
     *
     * @param username
     * @return
     */
    @GetMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<ServerResponse>> getUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("GET " + PATH_USER + "/" + username);

        return userRepository.findTopByUsername(username)
                .map(
                        foundUser -> ResponseEntity.ok().body(new ServerResponse(foundUser)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * update `user` based on the supplied `username`
     *
     * @param username
     * @param user
     * @return
     */
    @PatchMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<User>> updateByUsername(@PathVariable String username, @RequestBody User user) {
        printLastLineStackTrace("PATCH " + PATH_USER + "/" + username);
        user.setUsername(username);

        return userService.update(user)
                .then(Mono.just(ResponseEntity.accepted().body(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));

    }

    /**
     * delete `user` based on the supplied username
     *
     * @param username
     * @return
     */
    @DeleteMapping(PATH_USER + "/{username}")
    public Mono<ResponseEntity<Object>> deleteUserByUsername(@PathVariable String username) {
        printLastLineStackTrace("DEL " + PATH_USER + "/" + username);

        return userRepository.deleteByUsername(username)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * return all users (paginated)
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(PATH_USER)
    public Mono<Page<User>> listAllUsers(@RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        printLastLineStackTrace("GET " + PATH_USER + "s" + "&page=" + page + "&size=" + size);

        int skipCount = (page - 1) * size; // Calculate the number of items to skip

        var paginatedUsers = userRepository.findAll().skip(skipCount).take(size);

        var totalCount = userRepository.count();

        return Mono.zip(totalCount, paginatedUsers.collectList())
                .map(tuple -> new Page<>(tuple.getT1(), tuple.getT2(), page, size));
    }

}
