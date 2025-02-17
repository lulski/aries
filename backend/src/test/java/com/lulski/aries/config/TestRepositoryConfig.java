package com.lulski.aries.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.lulski.aries.post.Post;
import com.lulski.aries.post.PostRepository;
import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;
import java.time.LocalDateTime;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

/** For use in environment that doesn't have database (GitHub). */
@TestConfiguration
@EnableAutoConfiguration(exclude = {MongoReactiveRepositoriesAutoConfiguration.class})
@Profile("mock")
public class TestRepositoryConfig {

  private static final User mockAuthor =
      new User.UserBuilder()
          .username("dummyUser")
          .password("dummyPassword")
          .authorities(Set.of("ADMIN", "USER"))
          .firstname("bob")
          .lastname("gonzales")
          .email("bgz@www.com")
          .build();
  private static final Post mockPost =
      new Post(
          new ObjectId("1234b64f525959be00d07c0b"),
          "how to gitgood part two",
          "you just have to grind 3 hours everyday",
          mockAuthor.getUsername(),
          LocalDateTime.now(),
          LocalDateTime.now(),
          false,
          false);

  /**
   * mock of userRepository bean, enable or disable from test/resources/application.properties
   *
   * @return UserRepository
   */
  @Bean
  @Profile("mock")
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
              User user =
                  new User(
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
   * mock of postRepository bean, enable or disable from test/resources/application.properties
   *
   * @return PostRepository
   */
  @Bean
  @Profile("mock")
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
    return postRepository;
  }
}
