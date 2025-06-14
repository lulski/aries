package com.lulski.aries.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lulski.aries.config.TestMockRepositoryConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.post.exception.BadRequestException;
import com.lulski.aries.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000000")
@ActiveProfiles("mock")
@Import(TestWebSecurityConfig.class)
class PostRouterAndHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PostRepository postRepository;

    @Mock
    private ServerRequest mockServerRequest;

    private final User mockUser = new User.UserBuilder()
        .authorities(Set.of("USER"))
        .firstname("kocu")
        .lastname("gonzales")
        .password("Test")
        .build();

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

        User mockUser = new User.UserBuilder()
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

        User mockUser = new User.UserBuilder()
            .authorities(Set.of("USER"))
            .firstname("kocu")
            .lastname("gonzales")
            .password("Test")
            .build();

        webTestClient
            .mutateWith(mockUser(mockUser))
            .get()
            .uri("/posts?id=" + TestMockRepositoryConfig.mockPostId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
    }

    @Test
    void testErrorHandler() {

        when(postRepository.save(any())).thenThrow(new RuntimeException("error happened"));

        PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age", "");

        webTestClient
            .mutateWith(mockUser(mockUser))
            .post()
            .uri("/posts")
            .body(BodyInserters.fromValue(postRequestDto))
            .exchange()
            .expectStatus()
            .is5xxServerError();
    }

    @Test
    void findByTitle_WithSpecialCharacters() {
        String specialCharacterTitle = "Special%Character";
        Post expectedPost = new Post(new ObjectId(),
            specialCharacterTitle, "This is a post with special characters in the title",
            "testUser", LocalDateTime.now().minusDays(1), LocalDateTime.now(), true, false);

        when(postRepository.findTopByTitle(specialCharacterTitle)).thenReturn(Mono.just(expectedPost));

        when(mockServerRequest.queryParam("title")).thenReturn(Optional.of(specialCharacterTitle));

        webTestClient
            .mutateWith(mockUser(mockUser))
            .get()
            .uri("/posts?title=" + specialCharacterTitle)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(PostResponseDto.PostDto.class)
            .consumeWith((result) -> {
                PostResponseDto.PostDto responseBody = result.getResponseBody();
                System.out.println("Content-Type: "
                    + result.getResponseHeaders().getContentType());
                System.out.println("Response Body: " + responseBody);
                assertThat(responseBody).isNotNull();
                assertThat(responseBody.title()).isEqualTo(specialCharacterTitle);
                assertThat(responseBody.content()).isEqualTo(
                    "This is a post with special characters in the title");
                assertThat(responseBody.id()).isNotNull();
                assertThat(responseBody.createdOn()
                    .equals(LocalDateTime.now().minusDays(1)
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                            .withLocale(Locale.ENGLISH))));
                assertThat(responseBody.modifiedOn()
                    .equals(LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                            .withLocale(Locale.ENGLISH))));

            });
    }

    @Test
    void findByTitle_ShouldThrowBadRequestException() {
        when(mockServerRequest.queryParam("title")).thenReturn(Optional.empty());

        PostHandler postHandler = new PostHandler();

        Mono<ServerResponse> responseMono = postHandler.findByTitle(mockServerRequest);
        // Then
        StepVerifier.create(responseMono)
            .expectErrorSatisfies(error -> {
                assertThat(error)
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("invalid request: title query parameter is missing");
            })
            .verify();

        verify(mockServerRequest, times(1)).queryParam("title");

    }

}
