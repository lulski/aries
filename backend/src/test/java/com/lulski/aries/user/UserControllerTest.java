package com.lulski.aries.user;

import static com.lulski.aries.util.Constant.PATH_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.config.TestWebSecurityConfig;

import java.util.Arrays;
import java.util.Set;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWebTestClient(timeout = "100000000")
@ActiveProfiles("mock")
@Import(TestWebSecurityConfig.class)
class UserControllerTest {

    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private final User dummyUser = new User(
            null, "dummyUser", "dummyPassword", "just a dummy", "not mandatory", "dummy@xyz.com");
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
        Arrays.stream(cApplicationContext.getBeanDefinitionNames())
                .forEach(b -> System.out.println(">>> bean: " + b));
    }

    @Test
    @Order(1)
    void createNewUser() {

        User mockUser = new User.UserBuilder()
                .authorities(Set.of("USER"))
                .firstname("mock")
                .lastname("user")
                .password("test")
                .username("test")
                .build();

        UserRequestDto dto = new UserRequestDto(
                "bnana", "password", Set.of("ADMIN", "USER"), "berak", "nanah", "bnana@baba.com");

        webTestClient
                .mutateWith(mockUser(mockUser))
                .post()
                .uri(PATH_USER)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(UserControllerResponseDto.class)
                .value(
                        serverResponse -> {
                            var item = serverResponse.items();
                            assertThat(item).isNotEmpty();
                        });
    }

    @Test
    @Order(2)
    void getUserByUsername() {

        webTestClient
                .get()
                .uri(PATH_USER + "/" + this.dummyUser.getUsername())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserControllerResponseDto.class)
                .value(
                        serverResponse -> {
                            UserDto item = serverResponse.items().getFirst();
                            assertThat(item.username()).isEqualTo(this.dummyUser.getUsername());
                        });
    }
}
