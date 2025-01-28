package com.lulski.aries.user;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * User service
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * update User object
     *
     * @param userUpdate
     * @return
     */
    public Mono<User> update(User userUpdate) {
        return userRepository.findTopByUsername(userUpdate.getUsername())
                .flatMap(foundUser -> {
                    prepareUpdate(foundUser, userUpdate);
                    return userRepository.save(userUpdate);
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    /**
     * set originalUser fields with the values from userUpdate
     *
     * @param originalUser
     * @param userUpdate
     */
    public void prepareUpdate(User originalUser, User userUpdate) {
        userUpdate.setId(originalUser.getId());

        if (userUpdate.getFirstName() == null) {
            userUpdate.setFirstName(originalUser.getFirstName());
        }

        if (userUpdate.getLastName() == null) {
            userUpdate.setLastName(originalUser.getLastName());
        }

        if (userUpdate.getEmail() == null) {
            userUpdate.setEmail(originalUser.getEmail());
        }
    }
}
