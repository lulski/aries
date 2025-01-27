package com.lulski.aries.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Useful read <a href=
 * "https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html">mongo
 * repo doc</a>
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
    Flux<User> findAllByLastName(String value);

    Mono<User> findByEmail(String email);

    Mono<User> findTopByUsername(String username);

    Mono<User> deleteByUsername(String username);
}
