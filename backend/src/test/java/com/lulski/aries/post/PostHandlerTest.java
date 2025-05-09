package com.lulski.aries.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.lulski.aries.config.TestMockRepositoryConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.user.User;

import java.util.Arrays;
import java.util.Set;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000000")
@ActiveProfiles("mock")
@Import(TestWebSecurityConfig.class)
class PostHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PostRepository postRepository;

    @Test
    void contextLoad() {
        Arrays.stream(applicationContext.getBeanDefinitionNames())
            .forEach(b -> System.out.println(">> beans: " + b));
    }

    @Test
    void insertNew() {
        PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age", "");
        Post post = new Post();
        post.setId(new ObjectId());
        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());

        User mockUser =
            new User.UserBuilder()
                .authorities(Set.of("USER"))
                .firstname("kocu")
                .lastname("gonzales")
                .password("Test")
                .build();

        webTestClient
            .mutateWith(mockUser(mockUser))
            .post()
            .uri("/posts")
            .body(BodyInserters.fromValue(postRequestDto))
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }


    @Test
    void getById() {
        PostRequestDto postRequestDto = new PostRequestDto("", "", TestMockRepositoryConfig.mockPostId);

        Post post = new Post();
        post.setId(new ObjectId());
        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());

        User mockUser =
            new User.UserBuilder()
                .authorities(Set.of("USER"))
                .firstname("kocu")
                .lastname("gonzales")
                .password("Test")
                .build();

        webTestClient
            .mutateWith(mockUser(mockUser))
            .get()
            .uri("/posts/" + TestMockRepositoryConfig.mockPostId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test
    void testErrorHandler() {

        when(postRepository.save(any())).thenThrow(new RuntimeException("error happened"));

        PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age", "");

        User mockUser =
            new User.UserBuilder()
                .authorities(Set.of("USER"))
                .firstname("kocu")
                .lastname("gonzales")
                .password("Test")
                .build();

        webTestClient
            .mutateWith(mockUser(mockUser))
            .post()
            .uri("/posts")
            .body(BodyInserters.fromValue(postRequestDto))
            .exchange()
            .expectStatus()
            .is5xxServerError();
    }
}
