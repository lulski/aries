
package com.lulski.aries.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdFilterTest {

    @Test
    void whenRequestContainsCorrelationId_headerAndContextUseSameValue() {
        final String externalId = "external-corr-id-123";

        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(CorrelationIdFilter.HEADER_NAME, externalId)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        CorrelationIdFilter filter = new CorrelationIdFilter();

        WebFilterChain chain = new WebFilterChain() {
            @Override
            public Mono<Void> filter(ServerWebExchange serverWebExchange) {
                // verify reactive context contains the correlation id forwarded by the filter
                return Mono.deferContextual((ContextView ctx) -> {
                    assertTrue(ctx.hasKey(CorrelationIdFilter.CORRELATION_ID_KEY));
                    String ctxId = ctx.get(CorrelationIdFilter.CORRELATION_ID_KEY);
                    assertEquals(externalId, ctxId);
                    return Mono.empty();
                });
            }
        };

        // execute filter
        filter.filter(exchange, chain).block();

        // verify response header was set to the same value
        String header = exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.HEADER_NAME);
        assertEquals(externalId, header);
    }

    @Test
    void whenRequestDoesNotContainCorrelationId_filterGeneratesUuidAndSetsHeaderAndContext() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        CorrelationIdFilter filter = new CorrelationIdFilter();

        WebFilterChain chain = new WebFilterChain() {
            @Override
            public Mono<Void> filter(ServerWebExchange serverWebExchange) {
                return Mono.deferContextual((ContextView ctx) -> {
                    assertTrue(ctx.hasKey(CorrelationIdFilter.CORRELATION_ID_KEY));
                    String ctxId = ctx.get(CorrelationIdFilter.CORRELATION_ID_KEY);
                    assertNotNull(ctxId);
                    // ensure it's a valid UUID
                    UUID.fromString(ctxId);
                    return Mono.empty();
                });
            }
        };

        filter.filter(exchange, chain).block();

        String header = exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.HEADER_NAME);
        assertNotNull(header);
        // ensure the header value is a valid UUID as well
        UUID.fromString(header);
    }

}