package com.lulski.aries.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Integration tests for Post Router and Handler endpoints.
 * 
 * This test class validates the post-related API endpoints with:
 * - Mock repository beans for isolated testing
 * - Permissive test security configuration
 * - Reactive WebTestClient for testing reactive endpoints
 * - MongoDB autoconfiguration excluded via test properties
 * 
 * Activated with the "mock" profile to use in-memory data instead of MongoDB.
 */
@SpringBootTest
@ActiveProfiles("mock")
@Import({TestWebSecurityConfig.class, TestMockRepositoryConfig.class})
class PostRouterAndHandlerTest {

        @Autowired
        private WebTestClient webTestClient;

        @Autowired
        private PostRepository postRepository;

        private final User mockUser = new User.UserBuilder()
                        .authorities(Set.of("USER"))
                        .firstname("kocu")
                        .lastname("gonzales")
                        .password("Test")
                        .build();

        /**
         * Test successful creation of a new post.
         */
        @Test
        void insertNew() {
                PostRequestDto postRequestDto = new PostRequestDto("is this it", "modern age", "");

                webTestClient
                                .mutateWith(mockUser(mockUser))
                                .post()
                                .uri("/posts")
                                .body(BodyInserters.fromValue(postRequestDto))
                                .exchange()
                                .expectStatus()
                                .is2xxSuccessful();
        }

        /**
         * Test that posting with illegal characters in title returns 400 error.
         */
        @Test
        void whenTitleHasIllegalCharacter_then_return400Error() {
                PostRequestDto postRequestDto = new PostRequestDto("Contains illegal characters #", "modern age", "");

                webTestClient
                                .mutateWith(mockUser(mockUser))
                                .post()
                                .uri("/posts")
                                .body(BodyInserters.fromValue(postRequestDto))
                                .exchange()
                                .expectStatus()
                                .is4xxClientError();
        }

        /**
         * Test successful retrieval of a post by ID.
         */
        @Test
        void getById() {
                webTestClient
                                .mutateWith(mockUser(mockUser))
                                .get()
                                .uri("/posts?id=" + TestMockRepositoryConfig.mockPostId)
                                .exchange()
                                .expectStatus()
                                .is2xxSuccessful();
        }

        /**
         * Test error handling when repository throws exception.
         */
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

        /**
         * Test finding a post by title containing special characters.
         */
        @Test
        void findByTitle_WithSpecialCharacters() {
                String specialCharacterTitle = "Special%Character";
                Post expectedPost = new Post(new ObjectId(),
                                specialCharacterTitle, "This is a post with special characters in the title",
                                "testUser", LocalDateTime.now().minusDays(1), LocalDateTime.now(), true, false);

                when(postRepository.findTopByTitle(specialCharacterTitle)).thenReturn(Mono.just(expectedPost));

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
                                        assertThat(responseBody).isNotNull();
                                        assertThat(responseBody.title()).isEqualTo(specialCharacterTitle);
                                        assertThat(responseBody.titleUrl())
                                                        .isEqualTo(PostUtil.sanitizeTitleForURL(specialCharacterTitle));
                                        assertThat(responseBody.content()).isEqualTo(
                                                        "This is a post with special characters in the title");
                                        assertThat(responseBody.id()).isNotNull();
                                });
        }

        /**
         * Test that missing title parameter throws BadRequestException.
         */
        @Test
        void findByTitle_ShouldThrowBadRequestException() {
                // Create a mock ServerRequest without title parameter
                ServerRequest mockServerRequest = mock(ServerRequest.class);
                when(mockServerRequest.queryParam("title")).thenReturn(Optional.empty());

                PostHandler postHandler = new PostHandler();
                Mono<ServerResponse> responseMono = postHandler.findByTitle(mockServerRequest);

                // Verify the exception is thrown
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
