package com.lulski.aries.post;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public record PostResponseDto(List<PostDto> postDto, String message) {
    public static PostDto fromPost(Post post) {
        return new PostDto(post.getTitle(), post.getContent(), post.getAuthor(), post.getId().toString(),
            post.getCreatedOn().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH))
            , post.getModifiedOn().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH)));
    }

    /**
     * Convert List<Post> to List<PostDto>.
     */
    public static List<PostDto> fromPosts(List<Post> posts) {
        List<PostDto> list = new ArrayList<>();
        for (Post post : posts) {
            list.add(fromPost(post));
        }
        return list;
    }

    record PostDto(String title, String content, String author, String id, String createdOn,
                   String modifiedOn) {
    }
}
