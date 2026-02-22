package com.lulski.aries.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public class RabbitMessageListener {

    private final Logger logger = LoggerFactory.getLogger(RabbitMessageListener.class);

    @RabbitListener(queues = "#{@rabbitConfig.getQueueName()}")
    public void receiveMessage(String message) {
        logger.info("Received RabbitMQ message: {}", message);
        // TODO: add processing logic here
    }
}

