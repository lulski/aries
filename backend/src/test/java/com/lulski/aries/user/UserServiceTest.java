package com.lulski.aries.user;

import com.lulski.aries.config.TestMockRepositoryConfig;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("mock")
@Import(TestMockRepositoryConfig.class)
class UserServiceTest {

  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  @Autowired private ApplicationContext cApplicationContext;

  @Test
  void contextLoads() {
    Arrays.stream(cApplicationContext.getBeanDefinitionNames())
        .forEach(b -> System.out.println(">>> bean: " + b));
  }

  @Test
  void update() {
    // insert a user
    UserRequestDto userRequestDto =
        new UserRequestDto(
            "dummyUser", "dummyPassword", Set.of("USER"), "well", "develop", "wdevelop@gogo.com");

    userService.insertNew(userRequestDto).block();

    // then update the user
    UserRequestDto updatedUserRequestDto =
        new UserRequestDto("", "dummyPassword", Set.of("USER"), "updated", "updated", "updated");

    StepVerifier.create(userService.update(userRequestDto.username(), updatedUserRequestDto))
        .expectNextMatches(
            result -> {
              return result.getLastName().equals("updated")
                  && result.getFirstName().equals("updated")
                  && result.getEmail().equals("updated");
            })
        .verifyComplete();
  }

  @Test
  void insertNew() {
    UserRequestDto userRequestDto =
        new UserRequestDto(
            "dummyUser", "dummyPassword", Set.of("USER"), "well", "develop", "wdevelop@gogo.com");

    StepVerifier.create(userService.insertNew(userRequestDto))
        .expectNextMatches(
            result -> {
              // make sure that password field is encrypted
              return result.getId() != null && result.getPassword().startsWith("{bcrypt}");
            })
        .verifyComplete();
  }
}
