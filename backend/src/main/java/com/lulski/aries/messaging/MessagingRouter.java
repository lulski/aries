package com.lulski.aries.messaging;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class MessagingRouter {
    
    @Autowired
    MessagingHandler messagingHandler;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean(name = "messagingRoute")
    RouterFunction<ServerResponse> route(MessagingHandler messagingHandler) {
        // Define your routes here, e.g.:
        return RouterFunctions.route(POST("/messages"), messagingHandler::publishMessage);
    }



}
