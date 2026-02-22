package com.lulski.aries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

/**
 *
 */
@SpringBootApplication
@EnableRabbit
// @EnableReactiveMongoRepositories
public class AriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AriesApplication.class, args);
    }
}
