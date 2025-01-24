package com.lulski.aries.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import com.lulski.aries.config.MongoDBContainerUtil;
import com.lulski.aries.user.User;

import reactor.test.StepVerifier;

@DataMongoTest
@ContextConfiguration(classes = PostService.class)
@EnableAutoConfiguration
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    private final User author = new User(null, "rbelmont", "Richter", "Belmont", "rbelmont@xyz.com", false);
    private final Post post = new Post("how to gitgood", "you just have to grind 3 hours everyday",
            author, LocalDateTime.now(), LocalDateTime.now(), false, false);

    @BeforeAll
    static void setUp() {
        System.out.println(">>> Starting testcontainer:mongodb");
        MongoDBContainerUtil.getMongoDbContainer().start();
    }

    @AfterAll
    static void afterAll() {
        MongoDBContainerUtil.getMongoDbContainer().stop();
    }

    @Test
    void testArchivePost() {

    }

    @Test
    void testCreate() {
        StepVerifier.create(postRepository.save(post).single()).expectNextMatches(p -> {
            if (p.getId() != null)
                return true;
            else
                return false;
        }).verifyComplete();

    }
}
