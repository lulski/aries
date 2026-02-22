package com.lulski.aries.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.lulski.aries.config.TestWebSecurityConfig;

@SpringBootTest
@AutoConfigureWebTestClient(
    timeout = "PT5m" // 5 minutes timeout for all requests
)
@Import({TestWebSecurityConfig.class})
@ActiveProfiles("mock")
class MessagingRouterIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void route_postsToMessagesEndpoint_successfullyRoutesToHandler() {
        String payload = """
        {
            "exchange": "test-exchange",
            "routingKey": "test-routing-key",
            "message": "Hello"
        }
        """;

        webTestClient.post()
            .uri("/messages")
            .header("X-Custom-Header", "test-value")
            .header("Content-Type", "application/json")
            .bodyValue(payload)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void route_postsToMessagesWithCustomBody_rece6ivesRequest() {
        String payload = """
        {
            "exchange": "test-exchange",
            "routingKey": "test-routing-key",
            "message": "Hello"
        }
        """;
                
        webTestClient.post()
            .uri("/messages")
            .header("Content-Type", "application/json")
            .bodyValue(payload)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void route_postsToInvalidEndpoint_returnsNotFound() {
        webTestClient.post()
            .uri("/invalid")
            .bodyValue("test payload")
            .exchange()
            .expectStatus().isNotFound();
    }
}