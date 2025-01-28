package com.lulski.aries.post;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Post
 */
@Document(collection = "posts")
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class Post {

    private ObjectId id;

    @Indexed
    private String title;

    private String author;

    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private Boolean isPublished;
    private Boolean isArchived;

    /**
     * Post
     */
    public Post() {
    }

    /**
     * this is fun
     *
     * @param title
     * @param content
     * @param author
     * @param createdOn
     * @param modifiedOn
     * @param isPublished
     * @param isArchived
     */
    public Post(String title, String content, String author, LocalDateTime createdOn, LocalDateTime modifiedOn,
            Boolean isPublished,
            Boolean isArchived) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isPublished = isPublished;
        this.isArchived = isArchived;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP")
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

}
