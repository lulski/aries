package com.lulski.aries.post;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.config.TestRepositoryConfig;
import com.lulski.aries.user.User;

import reactor.test.StepVerifier;

@SpringBootTest
@Import(TestRepositoryConfig.class)
public class PostServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceTest.class);

    @Autowired
    private PostService postService;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private PostRepository postRepository;

    private final User author = new User(new ObjectId("6795b64f525959be00d07c0b"), "rbelmont", "Richter", "Belmont",
            "rbelmont@xyz.com", false);
    private final Post post = new Post(new ObjectId("1234b64f525959be00d07c0b"), "how to gitgood part two",
            "you just have to grind 3 hours everyday",
            author.getUsername(), LocalDateTime.now(), LocalDateTime.now(), false, false);

    @Autowired
    private ApplicationContext cApplicationContext;

    @Test
    void contextLoads() {
        Arrays.stream(cApplicationContext.getBeanDefinitionNames()).forEach(b -> System.out.println(">>> bean: " + b));
    }

    @BeforeEach
    void setUp() {
        if (profile.equals("testcontainer")) {
            System.out.println(">>> Starting testcontainer:mongodb");
            MongoDbContainerUtil.getMongoDbContainer().start();
        } else if (profile.equals("mock")) {
            logger.info("using mockBean");
        }
    }

    @AfterEach
    void afterAll() {
        if (profile.equals("testcontainer")) {
            System.out.println(">>> stopping testcontainer:mongodb");
            MongoDbContainerUtil.getMongoDbContainer().stop();
        }
    }

    @Test
    void createNewPostThenArchiveIt() {
        Arrays.stream(cApplicationContext.getBeanDefinitionNames()).forEach(b -> System.out.println(">>> bean: " + b));
        System.out.println("");

        Post postToBeArchived = new Post(new ObjectId("1234b64f525959be00d07c0b"), "test archiving a post",
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
