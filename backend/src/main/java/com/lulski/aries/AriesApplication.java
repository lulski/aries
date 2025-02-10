package com.lulski.aries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** */
@SpringBootApplication
// @EnableReactiveMongoRepositories
public class AriesApplication {

  public static void main(String[] args) {

    var context = SpringApplication.run(AriesApplication.class, args);
    System.out.println(">>> breakpoint");
    /*
     * String[] beanNames = context.getBeanDefinitionNames();
     * Arrays.stream(beanNames).forEach(System.out::println);
     */

  }
}
