package com.lulski.aries.post;

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
import org.springframework.test.context.ActiveProfiles;

import com.lulski.aries.config.MongoDbContainerUtil;
import com.lulski.aries.config.TestMockRepositoryConfig;
import com.lulski.aries.config.TestWebSecurityConfig;
import com.lulski.aries.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import reactor.test.StepVerifier;

@SpringBootTest
@Import({ TestMockRepositoryConfig.class, TestWebSecurityConfig.class })
@ActiveProfiles("mock")
public class PostServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceTest.class);
    private final User author = new User(
            new ObjectId("6795b64f525959be00d07c0b"),
            "rbelmont",
            "dummyPassword",
            "Richter",
            "Belmont",
            "rbelmont@xyz.com");
    private final Post post = new Post(
            new ObjectId("1234b64f525959be00d07c0b"),
            "how to gitgood part two",
            "you just have to grind 3 hours everyday",
            author.getUsername(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            false);
    @Autowired
    private PostService postService;
    @Value("${spring.profiles.active}")
    private String profile;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ApplicationContext cApplicationContext;

    @Test
    void contextLoads() {
        Arrays.stream(cApplicationContext.getBeanDefinitionNames())
                .forEach(b -> System.out.println(">>> bean: " + b));
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
        Arrays.stream(cApplicationContext.getBeanDefinitionNames())
                .forEach(b -> System.out.println(">>> bean: " + b));

        Post postToBeArchived = new Post(
                new ObjectId("1234b64f525959be00d07c0b"),
                "test archiving a post",
                "Help keep the library tidy by returning your dishes to the cafe",
                author.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                false);

        postRepository.save(postToBeArchived).block();

        StepVerifier.create(postService.archivePost(postToBeArchived).single())
                .expectNextMatches(p -> p.getIsArchived() == true)
                .verifyComplete();
    }

    @Test
    void createNewPost() {
        StepVerifier.create(postRepository.save(post).single())
                .expectNextMatches(p -> p.getId() != null)
                .verifyComplete();
    }

    @Test
    void listAllPosts() {
        StepVerifier.create(postService.listAll(1, 2).collectList())
                .expectNextMatches(
                        posts -> {
                            return !posts.isEmpty();
                        })
                .verifyComplete();
    }

    @Test
    void getById_ThenReturnAPost() {

        StepVerifier.create(postService.getById(TestMockRepositoryConfig.mockPostId))
                .expectNextMatches(post -> post.getId().equals(new ObjectId(TestMockRepositoryConfig.mockPostId)))
                .verifyComplete();
    }

    @Test
    void getByTitle_ThenReturnAPost() {
        StepVerifier.create(postService.getByTitle("how to gitgood part two"))
                .expectNextMatches(post -> post.getTitle().equals("how to gitgood part two"))
                .verifyComplete();
    }

    @Test
    void shouldUpdatePostWithNewTitleAndContent() {
        var updateDto = new PostRequestDto("how to gitgood part two has been updated", "just use AI",
                "1234b64f525959be00d07c0b");

        Post expected = new Post(
                new ObjectId("1234b64f525959be00d07c0b"),
                "how to gitgood part two has been updated",
                "just use AI",
                author.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                false);

        StepVerifier.create(postService.updatePost(updateDto, this.author))
                .expectNextMatches(updatedPost -> updatedPost.getTitle().equals(expected.getTitle()) &&
                        updatedPost.getContent().equals(expected.getContent()) &&
                        updatedPost.getAuthor().equals(expected.getAuthor()))
                .verifyComplete();
    }
}
