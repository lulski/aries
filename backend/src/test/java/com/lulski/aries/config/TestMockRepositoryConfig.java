package com.lulski.aries.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.lulski.aries.post.Post;
import com.lulski.aries.post.PostRepository;
import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * For use in environment that doesn't have database (GitHub).
 */
// @TestConfiguration
@Component
@EnableAutoConfiguration(exclude = { MongoReactiveRepositoriesAutoConfiguration.class })
@Profile("mock")
@Configuration
public class TestMockRepositoryConfig {

    public static final String mockPostId = "1234b64f525959be00d07c0b";

    private static final User mockAuthor = new User.UserBuilder()
            .username("dummyUser")
            .password("dummyPassword")
            .authorities(Set.of("ADMIN", "USER"))
            .firstname("bob")
            .lastname("gonzales")
            .email("bgz@www.com")
            .build();
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
     * mock of userRepository bean, enable or disable from
     * test/resources/application.properties
     *
     * @return UserRepository
     */
    @Bean
    @Primary
    public UserRepository userRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        invocation -> {
                            User argument = invocation.getArgument(0);
                            argument.setId(new ObjectId("67b2abe390f04d67290f4523"));
                            return Mono.just(argument);
                        });

        when(userRepository.findTopByUsername(anyString()))
                .thenAnswer(
                        inv -> {
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
     * mock of postRepository bean, enable or disable from
     * test/resources/application.properties
     *
     * @return PostRepository
     */
    @Bean
    @Primary
    public PostRepository postRepository() {
        PostRepository postRepository = mock(PostRepository.class);

        when(postRepository.save(any(Post.class)))
                .thenAnswer(
                        invocation -> {
                            Post argument = invocation.getArgument(0);
                            return Mono.just(argument);
                        });

        when(postRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(mockPost));
        // when(postRepository.findById(any(ObjectId.class))).thenReturn(Mono.error(new
        // RuntimeException("Whoops!")));

        var post1 = new Post();
        post1.setId(new ObjectId());
        post1.setTitle("post 1");
        post1.setContent("content 1");

        var post2 = new Post();
        post2.setId(new ObjectId());
        post2.setTitle("post 2");
        post2.setContent("content 2");

        when(postRepository.findAllBy(any(Pageable.class))).thenReturn(Flux.just(post1, post2));
        // Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,
        // "createdOn"));

        when(postRepository.save(any())).thenAnswer(invocationOnMock -> {
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

        when(postRepository.findTopByTitle(anyString())).thenAnswer(invocationOnMock -> {
            String title = invocationOnMock.getArgument(0);
            Post mockPost = new Post();
            mockPost.setTitle(title);
            mockPost.setContent("this is a mock post");
            mockPost.setCreatedOn(LocalDateTime.now());
            mockPost.setAuthor("mock author");
            return Mono.just(mockPost);
        });

        return postRepository;
    }
}
