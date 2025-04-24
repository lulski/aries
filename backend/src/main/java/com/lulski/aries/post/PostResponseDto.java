package com.lulski.aries.post;

import java.util.ArrayList;
import java.util.List;

public record PostResponseDto(List<PostDto> postDto, String message) {
  record PostDto(String title, String content, String author, String id) {}

  public static PostDto fromPost(Post post) {
    return new PostDto(post.getTitle(), post.getContent(), post.getAuthor(), post.getId().toString());
  }

  /** Convert List<Post> to List<PostDto>. */
  public static List<PostDto> fromPosts(List<Post> posts) {
    List<PostDto> list = new ArrayList<>();
    for (Post post : posts) {
      list.add(fromPost(post));
    }
    return list;
  }
}
