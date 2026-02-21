package com.lulski.aries.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class MessagingHandler {
    private final Logger logger = LoggerFactory.getLogger(MessagingHandler.class);


    @Autowired
    RabbitTemplate rabbitTemplate;

    public Mono<ServerResponse> publishMessage(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .flatMap(map -> {
                    Object exchange = map.get("exchange");
                    Object routingKey = map.get("routingKey");
                    Object message = map.get("message");

                    try {
                        if (exchange == null) {
                            String rk = routingKey != null ? String.valueOf(routingKey) : "messages";
                            rabbitTemplate.convertAndSend(rk, message);
                        } else {
                            rabbitTemplate.convertAndSend(String.valueOf(exchange), String.valueOf(routingKey), message);
                        }
                    } catch (Exception e) {
                        logger.warn("Failed to publish message to RabbitMQ", e);
                    }

                    return ServerResponse.ok().build();
                });
    }
}
