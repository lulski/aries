package com.lulski.aries.post;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
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

    public Post() {
    }

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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

}
