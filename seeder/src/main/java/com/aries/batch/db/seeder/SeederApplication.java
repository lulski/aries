package com.aries.batch.db.seeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeederApplication {

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(SeederApplication.class, args)));
  }
}
