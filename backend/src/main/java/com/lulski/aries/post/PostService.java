package com.lulski.aries.post;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class PostService {

    private PostRepository postRepository;

    public Mono<ResponseEntity<PostDTO>> archivePost(Post post) {

        post.setIsArchived(true);
        return this.postRepository.save(post)
                .map(p -> ResponseEntity.accepted().body(new PostDTO("OK")))
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(new PostDTO("failed"))));
    }

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
