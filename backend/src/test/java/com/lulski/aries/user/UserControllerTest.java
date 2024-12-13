package com.lulski.aries.user;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HeaderElement;
import com.lulski.aries.config.MongoDBContainerUtil;
import com.lulski.aries.config.TestMongoConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.dto.ServerResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.header.Header;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;

import static com.lulski.aries.util.Constant.PATH_USER;

@WebFluxTest(controllers = UserController.class)
@Import({TestWebSecurityConfig.class, TestMongoConfig.class, UserService.class})
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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
    void addUser() {
        User newUser = new User(null, "dummyUser", "dummy", "",
                "dummy@dummy.com", false);

        webTestClient.post()
                .uri(PATH_USER)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(newUser))
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(ServerResponse.class).value(serverResponse -> {
                    assert Objects.nonNull(serverResponse.getItem().getId());
                    assert Objects.equals(serverResponse.getItem().getUsername(), newUser.getUsername());
                        }
                );
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void updateByUsername() {
    }

    @Test
    void deleteUserByUsername() {
    }

    @Test
    void listAllUsers() {
    }
}