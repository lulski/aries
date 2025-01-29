package com.lulski.aries.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.lulski.aries.post.Post;
import com.lulski.aries.post.PostRepository;
import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;

import reactor.core.publisher.Mono;

/**
 * For use in environment that doesn't have database (GitHub)
 */
@TestConfiguration
@EnableAutoConfiguration(exclude = { MongoReactiveRepositoriesAutoConfiguration.class })
// @EnableReactiveMongoRepositories
public class TestRepositoryConfig {

    private static final User mockAuthor = new User(new ObjectId("6795b64f525959be00d07c0b"), "dummyUser",
            "just a dummy", "not mandatory", "dummy@xyz.com",
            false);
    private static final Post mockPost = new Post(new ObjectId("1234b64f525959be00d07c0b"), "how to gitgood part two",
            "you just have to grind 3 hours everyday",
            mockAuthor.getUsername(), LocalDateTime.now(), LocalDateTime.now(), false, false);

    /**
     * mock of userRepository bean, enable or disable from
     * test/resources/application.properties
     *
     * @return UserRepository
     */
    @Bean
    @Profile("mock")
    @Primary
    public UserRepository userRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User argument = invocation.getArgument(0);
            argument.setId(mockAuthor.getId());
            return Mono.just(argument);
        });

        when(userRepository.findTopByUsername(anyString())).thenAnswer(inv -> {
            User user = new User(mockAuthor.getId(), inv.getArgument(0), mockAuthor.getFirstName(),
                    mockAuthor.getLastName(), mockAuthor.getEmail(), mockAuthor.isArchived());
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
    @Profile("mock")
    @Primary
    public PostRepository postRepository() {
        PostRepository postRepository = mock(PostRepository.class);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post argument = invocation.getArgument(0);
            return Mono.just(argument);
        });
        when(postRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(mockPost));
        return postRepository;
    }

}
