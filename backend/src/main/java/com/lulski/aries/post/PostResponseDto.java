package com.lulski.aries.post;

import java.util.ArrayList;
import java.util.List;

public record PostResponseDto(List<PostDto> postDto, String message) {
  record PostDto(String title, String content, String author) {}

  public static PostDto fromPost(Post post) {
    return new PostDto(post.getTitle(), post.getContent(), post.getAuthor());
  }

  public static List<PostDto> fromPosts(List<Post> posts) {
    List<PostDto> list = new ArrayList<PostDto>();
    for (Post post : posts) {
      list.add(fromPost(post));
    }
    return list;
  }
}
