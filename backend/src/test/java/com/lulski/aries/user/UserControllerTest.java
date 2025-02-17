package com.lulski.aries.user;

import static com.lulski.aries.util.Constant.PATH_USER;
import static org.assertj.core.api.Assertions.assertThat;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.config.TestDbMongoConfig;
import com.lulski.aries.config.TestRepositoryConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.config.TestcontainerMongoConfig;
import com.lulski.aries.dto.ServerResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest()
@Import({
  TestcontainerMongoConfig.class,
  TestWebSecurityConfig.class,
  TestDbMongoConfig.class,
  TestRepositoryConfig.class,
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWebTestClient(timeout = "100000000")
@ActiveProfiles(value = {"test", "withDb"})
class UserControllerTest {

  @TestConfiguration
  static class UserControllerTestConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      return new UserService(userRepository, passwordEncoder);
    }

    @Bean
    UserController userController(UserRepository userRepository, UserService userService) {
      return new UserController(userRepository, userService);
    }
  }

  private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

  private final User dummyUser =
      new User(
          null, "dummyUser", "dummyPassword", "just a dummy", "not mandatory", "dummy@xyz.com");
  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  @Value("${spring.profiles.active}")
  private String profile;

  @Autowired private ApplicationContext cApplicationContext;

  @BeforeEach
  void setUpDb() {
    if ("testContainer".equals(profile)) {
      System.out.println(">>> Starting testcontainer:mongodb");
      MongoDbContainerUtil.getMongoDbContainer().start();
    } else if ("mock".equals(profile)) {
      logger.info("using mockBean");
    }
  }

  @AfterEach
  void tearDownDb() {
    if ("testcontainer".equals(profile)) {
      System.out.println(">>> Stopping testcontainer:mongodb");
      MongoDbContainerUtil.getMongoDbContainer().stop();
    }
  }

  @BeforeEach
  void setUp() {
    System.out.println(">>> beforeEach");
  }

  @Test
  void contextLoads() {
    Arrays.stream(cApplicationContext.getBeanDefinitionNames())
        .forEach(b -> System.out.println(">>> bean: " + b));
  }

  @Test
  @Order(1)
  void createNewUser() {
    UserRequestDto dto =
        new UserRequestDto(
            "bnana", "password", Set.of("ADMIN", "USER"), "berak", "nanah", "bnana@baba.com");
    webTestClient
        .post()
        .uri(PATH_USER)
        .accept(MediaType.ALL)
        .body(BodyInserters.fromValue(dto))
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(ServerResponse.class)
        .value(
            serverResponse -> {
              assert Objects.equals(serverResponse.getItem().username(), dto.username());
              assert Objects.nonNull(serverResponse.getItem().id());
              assert serverResponse.getItem().authorities().contains("ADMIN");
              assert serverResponse.getItem().authorities().contains("USER");
            });
  }

  @Test
  @Order(2)
  void getUserByUsername() {
    Mono<User> monoUserSaveResponse = userRepository.save(this.dummyUser).single();

    StepVerifier.create(monoUserSaveResponse)
        .expectNextMatches(
            user -> {
              User savedUser = (User) user;
              System.out.println(savedUser.getId());
              userRepository.findTopByUsername(savedUser.getUsername());
              return true;
            })
        .expectComplete()
        .verify();

    webTestClient
        .get()
        .uri(PATH_USER + "/" + this.dummyUser.getUsername())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(ServerResponse.class)
        .value(
            serverResponse -> {
              assertThat(serverResponse.getItem().username())
                  .isEqualTo(this.dummyUser.getUsername());
            });
  }
}
