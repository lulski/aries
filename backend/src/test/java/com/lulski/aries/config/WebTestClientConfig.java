package com.lulski.aries.config;

import java.time.Duration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.netty.http.client.HttpClient;

@TestConfiguration
public class WebTestClientConfig {

    @Bean
    public WebTestClient webTestClient() {

        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ZERO);

        return WebTestClient.bindToController(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
