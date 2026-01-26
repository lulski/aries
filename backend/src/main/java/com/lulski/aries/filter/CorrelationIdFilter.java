package com.lulski.aries.filter;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements WebFilter {

    public static final String CORRELATION_ID_KEY = "correlationId";
    public static final String HEADER_NAME = "X-Correlation-ID";

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final String externalCorrId = exchange.getRequest().getHeaders().getFirst(HEADER_NAME);

        final String corrId;
        if (externalCorrId == null || externalCorrId.isBlank()) {
            corrId = UUID.randomUUID().toString();
        } else {
            corrId = externalCorrId;
        }

        exchange.getResponse().getHeaders().add(HEADER_NAME, corrId);

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(CORRELATION_ID_KEY, corrId));
    }
}
