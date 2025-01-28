package com.lulski.aries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 *
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class AriesApplication {

    public static void main(String[] args) {

        SpringApplication.run(AriesApplication.class, args);
        /*
         * String[] beanNames = context.getBeanDefinitionNames();
         * Arrays.stream(beanNames).forEach(System.out::println);
         */

    }

}
