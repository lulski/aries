package com.lulski.aries.post;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.user.User;

import reactor.test.StepVerifier;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private final User author = new User(new ObjectId("6795b64f525959be00d07c0b"), "rbelmont", "Richter", "Belmont",
            "rbelmont@xyz.com", false);
    private final Post post = new Post("how to gitgood part two", "you just have to grind 3 hours everyday",
            author.getUsername(), LocalDateTime.now(), LocalDateTime.now(), false, false);

    @BeforeAll
    static void setUp() {
        System.out.println(">>> Starting testcontainer:mongodb");
        MongoDbContainerUtil.getMongoDbContainer().start();

    }

    @AfterAll
    static void afterAll() {
        MongoDbContainerUtil.getMongoDbContainer().stop();
    }

    @Test
    void createNewPostThenArchiveIt() {
        Post postToBeArchived = new Post("test archiving a post",
                "Help keep the library tidy by returning your dishes to the cafe",
                author.getUsername(), LocalDateTime.now(), LocalDateTime.now(), false, false);

        postRepository.save(postToBeArchived).block();

        StepVerifier.create(postService.archivePost(postToBeArchived).single())
                .expectNextMatches(p -> p.getIsArchived() == true)
                .verifyComplete();
    }

    @Test
    void createNewPost() {
        StepVerifier.create(postRepository.save(post).single()).expectNextMatches(p -> p.getId() != null)
                .verifyComplete();

    }
}
