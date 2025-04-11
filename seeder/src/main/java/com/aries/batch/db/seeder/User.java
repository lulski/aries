package com.aries.batch.db.seeder;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public record User(
    String username,
    String password,
    String firstName,
    String lastName,
    String email,
    List<String> authorities) {}
