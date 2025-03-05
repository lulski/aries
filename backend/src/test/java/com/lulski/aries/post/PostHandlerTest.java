package com.lulski.aries.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.user.User;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000000")
@ActiveProfiles("test")
@Import(TestWebSecurityConfig.class)
class PostHandlerTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private ApplicationContext applicationContext;

  @MockitoBean private PostService postService;

  @Test
  void contextLoad() {
    Arrays.stream(applicationContext.getBeanDefinitionNames())
        .forEach(b -> System.out.println(">> beans: " + b));
  }

  @Test
  void insertNew() {
    PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age");

    User mockUser =
        new User.UserBuilder()
            .authorities(Set.of("USER"))
            .firstname("kocu")
            .lastname("gonzales")
            .password("Test")
            .build();

    webTestClient
        //        .mutateWith(mockUser("test").password("test").authorities("USER"))
        .mutateWith(mockUser(mockUser))
        .post()
        .uri("/posts")
        .body(BodyInserters.fromValue(postRequestDto))
        .exchange()
        .expectStatus()
        .is2xxSuccessful();
  }

  @Test
  void testErrorHandler() {

    when(postService.insertNew(any(), any())).thenThrow(new RuntimeException("error happened"));

    PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age");

    User mockUser =
        new User.UserBuilder()
            .authorities(Set.of("USER"))
            .firstname("kocu")
            .lastname("gonzales")
            .password("Test")
            .build();

    webTestClient
        //        .mutateWith(mockUser("test").password("test").authorities("USER"))
        .mutateWith(mockUser(mockUser))
        .post()
        .uri("/posts")
        .body(BodyInserters.fromValue(postRequestDto))
        .exchange()
        .expectStatus()
        .is5xxServerError();
  }
}
