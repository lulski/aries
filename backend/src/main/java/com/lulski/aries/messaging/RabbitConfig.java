package com.lulski.aries.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.queue.name:test-queue}")
    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    @Bean
    public Queue messagesQueue() {
        return new Queue(queueName, true, false, false);
    }


}