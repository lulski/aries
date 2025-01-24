package com.lulski.aries.user;

import com.lulski.aries.config.MongoDBContainerUtil;
import com.lulski.aries.config.TestDbMongoConfig;
import com.lulski.aries.config.TestcontainerMongoConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.dto.ServerResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.lulski.aries.util.Constant.PATH_USER;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = UserController.class)
@Import({ UserService.class, TestcontainerMongoConfig.class, TestWebSecurityConfig.class, TestDbMongoConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    private final User dummyUser = new User(null, "dummyUser", "just a dummy", "not mandatory", "dummy@xyz.com", false);
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setUpDb() {
        System.out.println(">>> Starting testcontainer:mongodb");
        MongoDBContainerUtil.getMongoDbContainer().start();
    }

    @AfterAll
    static void tearDownDb() {
        System.out.println(">>> Stopping testcontainer:mongodb");
        MongoDBContainerUtil.getMongoDbContainer().stop();
    }

    @BeforeEach
    void setUp() {
        System.out.println(">>> beforeEach");
    }

    @Test
    @Order(1)
    void addUser() {
        webTestClient.post()
                .uri(PATH_USER)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(this.dummyUser))
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(ServerResponse.class).value(serverResponse -> {
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
    /*
     * @Test
     * void updateByUsername() {
     * }
     * 
     * @Test
     * void deleteUserByUsername() {
     * }
     * 
     * @Test
     * void listAllUsers() {
     * }
     */
}