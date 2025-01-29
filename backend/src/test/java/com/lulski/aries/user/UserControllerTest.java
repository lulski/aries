package com.lulski.aries.user;

import static com.lulski.aries.util.Constant.PATH_USER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Objects;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.config.TestDbMongoConfig;
import com.lulski.aries.config.TestRepositoryConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.config.TestcontainerMongoConfig;
import com.lulski.aries.dto.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Import({ TestcontainerMongoConfig.class, TestWebSecurityConfig.class, TestDbMongoConfig.class,
        TestRepositoryConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000000")
class UserControllerTest {
    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private final User dummyUser = new User(null, "dummyUser", "just a dummy", "not mandatory", "dummy@xyz.com",
            false);
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private ApplicationContext cApplicationContext;

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
        Arrays.stream(cApplicationContext.getBeanDefinitionNames()).forEach(b -> System.out.println(">>> bean: " + b));
    }

    @Test
    @Order(1)
    void createNewUser() {
        webTestClient.post()
                .uri(PATH_USER)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(this.dummyUser))
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(ServerResponse.class)
                .value(serverResponse -> {
                    logger.info("serverResponse: " + serverResponse.getItem().getUsername());
                    logger.info("dummyUser: " + dummyUser.getUsername());
                    assert Objects.nonNull(serverResponse.getItem().getId());
                    assert Objects.equals(serverResponse.getItem().getUsername(), this.dummyUser.getUsername());
                });
    }

    @Test
    @Order(2)
    void getUserByUsername() {
        Mono<User> monoUserSaveResponse = userRepository.save(this.dummyUser).single();

        StepVerifier.create(monoUserSaveResponse)
                .expectNextMatches(user -> {
                    User savedUser = (User) user;
                    System.out.println(savedUser.getId());
                    userRepository.findTopByUsername(savedUser.getUsername());
                    return true;
                })
                .expectComplete().verify();

        webTestClient.get().uri(PATH_USER + "/" + this.dummyUser.getUsername())
                .exchange().expectStatus().isOk().expectBody(ServerResponse.class).value(
                        serverResponse -> {
                            assertThat(serverResponse.getItem().getUsername()).isEqualTo(this.dummyUser.getUsername());
                            assertThat(serverResponse.getItem().getEmail()).isEqualTo(this.dummyUser.getEmail());
                            assertThat(serverResponse.getItem().getLastName()).isEqualTo(this.dummyUser.getLastName());
                        });

    }
}
