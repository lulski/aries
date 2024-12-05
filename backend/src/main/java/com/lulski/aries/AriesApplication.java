package com.lulski.aries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Arrays;

/**
 *
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class AriesApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AriesApplication.class, args);
/*        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.stream(beanNames).forEach(System.out::println);*/

    }

}
