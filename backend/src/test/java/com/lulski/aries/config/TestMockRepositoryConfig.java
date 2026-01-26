package com.lulski.aries.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;

import com.lulski.aries.post.Post;
import com.lulski.aries.post.PostRepository;
import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Test configuration providing mock implementations of repository beans.
 * 
 * This configuration is activated when the "mock" profile is active and is primarily
 * used in CI/CD environments (e.g., GitHub) where MongoDB is not available.
 * 
 * Each repository bean is configured with @Primary to override the actual
 * MongoDB repositories in the Spring context during tests.
 * 
 * Usage: Activate with @ActiveProfiles("mock") in test classes.
 */
@TestConfiguration
@Profile("mock")
public class TestMockRepositoryConfig {

    public static final String mockPostId = "1234b64f525959be00d07c0b";

    /**
     * Mock user data used for testing post functionality.
     */
    private static final User mockAuthor = new User.UserBuilder()
            .username("dummyUser")
            .password("dummyPassword")
            .authorities(Set.of("ADMIN", "USER"))
            .firstname("bob")
            .lastname("gonzales")
            .email("bgz@www.com")
            .build();

    /**
     * Mock post data for repository responses.
     */
    private static final Post mockPost = new Post(
            new ObjectId(mockPostId),
            "how to gitgood part two",
            "you just have to grind 3 hours everyday",
            mockAuthor.getUsername(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            false);

    /**
     * Provides a mocked UserRepository bean for testing.
     *
     * Behaviors:
     * - save() returns a Mono containing the user with a generated ID
     * - findTopByUsername() returns a Mono with user data based on the queried username
     *
     * @return mocked UserRepository bean
     */
    @Bean
    @Primary
    public UserRepository userRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User argument = invocation.getArgument(0);
                    argument.setId(new ObjectId("67b2abe390f04d67290f4523"));
                    return Mono.just(argument);
                });

        when(userRepository.findTopByUsername(anyString()))
                .thenAnswer(inv -> {
                    User user = new User(
                            mockAuthor.getId(),
                            inv.getArgument(0),
                            "dummyPassword",
                            mockAuthor.getFirstName(),
                            mockAuthor.getLastName(),
                            mockAuthor.getEmail());
                    return Mono.just(user);
                });
        
        return userRepository;
    }

    /**
     * Provides a mocked PostRepository bean for testing.
     *
     * Behaviors:
     * - save() returns the post with a generated ID if none exists
     * - findById() returns the static mockPost
     * - findAllBy() returns a flux of two sample posts
     * - findTopByTitle() returns a post with the queried title
     *
     * @return mocked PostRepository bean
     */
    @Bean
    @Primary
    public PostRepository postRepository() {
        PostRepository postRepository = mock(PostRepository.class);

        // Configure save behavior for new posts
        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> {
                    Post argument = invocation.getArgument(0);
                    return Mono.just(argument);
                });

        // Configure findById to return mock post
        when(postRepository.findById(any(ObjectId.class)))
                .thenReturn(Mono.just(mockPost));

        // Configure findAllBy for paginated queries
        Post post1 = new Post();
        post1.setId(new ObjectId());
        post1.setTitle("post 1");
        post1.setContent("content 1");

        Post post2 = new Post();
        post2.setId(new ObjectId());
        post2.setTitle("post 2");
        post2.setContent("content 2");

        when(postRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(post1, post2));

        // Configure save with ID generation logic
        when(postRepository.save(any()))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0);
                    if (post != null && post.getId() == null) {
                        post.setId(new ObjectId());
                        return Mono.just(post);
                    } else if (post != null && post.getId() != null) {
                        return Mono.just(post);
                    } else {
                        return Mono.just(post1);
                    }
                });

        // Configure findTopByTitle for title-based searches
        when(postRepository.findTopByTitle(anyString()))
                .thenAnswer(invocationOnMock -> {
                    String title = invocationOnMock.getArgument(0);
                    Post titleMockPost = new Post();
                    titleMockPost.setTitle(title);
                    titleMockPost.setContent("this is a mock post");
                    titleMockPost.setCreatedOn(LocalDateTime.now());
                    titleMockPost.setAuthor("mock author");
                    return Mono.just(titleMockPost);
                });

        return postRepository;
    }
}
