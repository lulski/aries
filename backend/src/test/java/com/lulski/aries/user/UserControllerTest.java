package com.lulski.aries.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testPostUser(){

        MultiValueMap mapData = new LinkedMultiValueMap<>();
        mapData.add("username", "test_user");
        mapData.add("password", "abcdefg");


        webTestClient.post().uri("api/v1/user")
                .body(BodyInserters.fromFormData(mapData))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }


}