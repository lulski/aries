package com.lulski.aries.post;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Retrieves all posts from the repository.
     * <p>
     * This method fetches all posts stored in the database. If an error occurs during
     * the retrieval process, it logs the error and returns an empty Flux. It also maps
     * specific exceptions to custom exception types for better error handling.
     *
     * @return A Flux<Post> containing all posts in the repository. If an error occurs,
     * an empty Flux is returned.
     * @throws DatabaseAccessException if there's an error accessing the database,
     *                                 wrapped from DataAccessException.
     * @throws NetworkTimeoutException if there's a network timeout while accessing
     *                                 the database, wrapped from TimeoutException.
     */
    public Flux<Post> listAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));

        return this.postRepository
            .findAllBy(pageable)
            .onErrorResume(
                error -> {
                    LOGGER.error(">>> failed to fetch Post data: " + error.getMessage());
                    return Flux.empty();
                })
            .onErrorMap(DataAccessException.class, DatabaseAccessException::new)
            .onErrorMap(TimeoutException.class, NetworkTimeoutException::new);
    }

    public Mono<Long> countAllPosts() {
        return postRepository.count();
    }

    /**
     * Retrieves a Post by its unique identifier.
     * <p>
     * This method searches for a post in the repository with the given ID.
     * If no post is found, it throws a PostNotFoundException. It also handles
     * database access and network timeout errors.
     *
     * @param id The unique identifier of the post to retrieve, as a String.
     * @return A Mono<Post> containing the post with the matching ID if found.
     * @throws PostNotFoundException   if no post with the given ID is found.
     * @throws DatabaseAccessException if there's an error accessing the database.
     * @throws NetworkTimeoutException if there's a network timeout while accessing
     *                                 the database.
     */
    public Mono<Post> getById(String id) {

        return this.postRepository.findById(new ObjectId(id))
            .switchIfEmpty(
                Mono.error(new PostNotFoundException(id)))
            .onErrorMap(DataAccessException.class, DatabaseAccessException::new)
            .onErrorMap(TimeoutException.class, NetworkTimeoutException::new);
    }

    /**
     * Retrieves a Post with an exact matching title.
     * <p>
     * This method searches for a post in the repository with a title that exactly
     * matches the provided string.
     * If no post is found, it throws a PostNotFoundException. It also handles
     * database access and network timeout errors.
     *
     * @param title The exact title of the post to retrieve.
     * @return A Mono<Post> containing the post with the matching title if found.
     * @throws PostNotFoundException   if no post with the given title is found.
     * @throws DatabaseAccessException if there's an error accessing the database.
     * @throws NetworkTimeoutException if there's a network timeout while accessing
     *                                 the database.
     */
    public Mono<Post> getByTitle(String title) {
        return this.postRepository.findTopByTitle(title)
            .switchIfEmpty(
                Mono.error(new PostNotFoundException(title)))
            .onErrorMap(DataAccessException.class, DatabaseAccessException::new)
            .onErrorMap(TimeoutException.class, NetworkTimeoutException::new);
    }

    /**
     * Inserts a new post into the database.
     *
     * @param postRequestDto The data transfer object containing the necessary
     *                       information for creating a new post.
     * @param user           The user who is creating the post.
     * @return A {@link Mono} containing the newly created post.
     * @throws DatabaseAccessException If there is an error accessing the database.
     * @throws NetworkTimeoutException If there is a network timeout while accessing
     *                                 the database.
     */
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
     * Archives a post by setting its isArchived field to true.
     * <p>
     * This method performs a repository lookup for the supplied post using its ID,
     * then sets the isArchived field to true. If the post is not found, it throws a
     * RuntimeException.
     *
     * @param post The Post object to be archived. It must contain a valid ID for
     *             lookup.
     * @return A Mono<Post> containing the archived post if successful.
     * @throws RuntimeException if no post with the given ID is found in the
     *                          repository.
     */
    public Mono<Post> archivePost(Post post) {
        return postRepository
            .findById(post.getId())
            .doOnNext(p -> LOGGER.info(">>> found post with id : " + p.getId()))
            .switchIfEmpty(
                Mono.error(new RuntimeException("No post with Id: " + post.getId() + " is found")))
            .flatMap(
                found -> {
                    found.setIsArchived(true);
                    return postRepository.save(found);
                })
            .doOnSuccess(p -> LOGGER.info("post Id: " + p.getId() + " is archived"))
            .doOnError(err -> LOGGER.error("unable to complete operation for p: " + err.getMessage()));
    }
}
