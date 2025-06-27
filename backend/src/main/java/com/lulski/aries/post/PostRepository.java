package com.lulski.aries.post;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repo for Post collections
 */
@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, ObjectId> {

    Flux<Post> findAllByAuthor(String author);

    Mono<Post> findTopByTitle(String title);

    Flux<Post> findAllBy(Pageable pageable);

    Mono<Long> count();
}
