package com.lulski.aries.post;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveMongoRepository<Post, ObjectId> {

    Flux<Post> findAllByAuthor(String author);

    Mono<Post> findByTitle(String title);

}