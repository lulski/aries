package com.lulski.aries.post;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.lulski.aries.post.exception.DatabaseAccessException;
import com.lulski.aries.post.exception.NetworkTimeoutException;
import com.lulski.aries.post.exception.PostNotFoundException;
import com.lulski.aries.user.User;

import io.netty.handler.timeout.TimeoutException;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service for Post.
 */
@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Flux<Post> listAll() {
        return this.postRepository
            .findAll()
            .onErrorResume(
                error -> {
                    logger.error(">>> failed to fetch Post data: " + error.getMessage());
                    return Flux.empty();
                });
    }

    public Mono<Post> getById(String id) {

        return this.postRepository.findById(new ObjectId(id))
            .switchIfEmpty(
                Mono.error(new PostNotFoundException(id))
            ).onErrorMap(DataAccessException.class, DatabaseAccessException::new)
            .onErrorMap(TimeoutException.class, NetworkTimeoutException::new);
    }

    public Mono<Post> insertNew(PostRequestDto postRequestDto, User user) {
        Post post = new Post();
        post.setAuthor(user.getFirstName() + " " + user.getLastName());
        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());
        post.setCreatedOn(LocalDateTime.now());
        post.setModifiedOn(LocalDateTime.now());

        return postRepository.save(post);
    }

    /**
     * Will do repository lookup for the supplied post then set isArchived field to true.
     *
     * @param post
     * @return
     */
    public Mono<Post> archivePost(Post post) {
        return postRepository
            .findById(post.getId())
            .doOnNext(p -> System.out.println(">>> found post with id : " + p.getId()))
            .switchIfEmpty(
                Mono.error(new RuntimeException("No post with Id: " + post.getId() + " is found")))
            .flatMap(
                found -> {
                    found.setIsArchived(true);
                    return postRepository.save(found);
                })
            .doOnSuccess(p -> logger.info("post Id: " + p.getId() + " is archived"))
            .doOnError(err -> logger.error("unable to complete operation for p: " + err.getMessage()));
    }
}
