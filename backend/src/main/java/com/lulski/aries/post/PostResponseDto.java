package com.lulski.aries.post;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public record PostResponseDto(List<PostDto> postDto, String message, int page, int size, long total) {
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.ENGLISH);

    public static PostDto fromPost(Post post) {
        if (post == null)
            throw new IllegalArgumentException("post must not be null");

        return new PostDto(Objects.toString(post.getTitle(), "-"),
                PostUtil.sanitizeTitleForURL(post.getTitle()),
                Objects.toString(post.getContent(), "-"),
                Objects.toString(post.getAuthor(), "-"), Objects.toString(post.getId().toString(), "-"),
                post.getCreatedOn() == null ? "-" : post.getCreatedOn().format(FORMATTER),
                post.getModifiedOn() == null ? "-" : post.getCreatedOn().format(FORMATTER));
    }

    /**
     * Convert List<Post> to List<PostDto>.
     */
    public static List<PostDto> fromPosts(List<Post> posts) {
        if (posts == null)
            throw new IllegalArgumentException("posts must not be null");

        List<PostDto> list = new ArrayList<>();
        for (Post post : posts) {
            list.add(fromPost(post));
        }
        return list;
    }

    record PostDto(String title, String titleUrl, String content, String author, String id, String createdOn,
            String modifiedOn) {
    }
}
