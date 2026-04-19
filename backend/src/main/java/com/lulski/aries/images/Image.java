package com.lulski.aries.images;

import java.util.Objects;

/**
 * Sub-document structure for Post Images.
 * Stored as an array within the BlogPost collection.
 */
public class Image {

    private String imageUrl;
    private String caption;
    private Integer orderIndex;
    private String contentType;


    // Default Constructor required for Jackson mapping and JPA/Mongo DB embedding
    public Image() {}

    /**
     * Fluent Builder to construct Image instances immutably.
     */
    public static class Builder {
        private String imageUrl;
        private String caption;
        private Integer orderIndex;
        private String contentType;

        public static Builder newImage() { return new Builder(); }

        public Builder withUrl(String url) {
            this.imageUrl = Objects.requireNonNull(url, "imageUrl cannot be null");
            return this;
        }

        public Builder withCaption(String caption) {
            this.caption = Objects.requireNonNull(caption, "caption cannot be null");
            return this;
        }

        public Builder withOrder(Integer order) {
            this.orderIndex = order;
            return this;
        }

        public Builder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Creates the Image instance.
         */
        public Image build() {
            Image image = new Image();
            if (imageUrl != null) image.imageUrl = imageUrl;
            if (caption != null) image.caption = caption;
            if (orderIndex != null) image.orderIndex = orderIndex;
            if (contentType != null) image.contentType = contentType;
            return image;
        }
    }

    // Getters & Setters
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image image = (Image) o;
        // Equality is primarily driven by the identity of the URL here,
        // as per snippet design. In production, consider using Arrays.asList(fields).equals
        return Objects.equals(this.imageUrl, image.imageUrl); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageUrl='" + imageUrl + '\'' +
                ", caption='" + caption + '\'' +
                ", orderIndex=" + orderIndex +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
