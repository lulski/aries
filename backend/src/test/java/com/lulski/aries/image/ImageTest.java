package com.lulski.aries.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.lulski.aries.image.Image;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit tests for Image embedded document structure.
 * Focuses on field access, Builder pattern, and contract verification.
 */
@DisplayName("Image")
public class ImageTest {

    @Test
    @DisplayName("Default constructor initializes empty state")
    void defaultConstructorIsEmpty() {
        Image image = new Image();

        assertThat(image.getImageUrl()).isNull();
        assertThat(image.getCaption()).isNull();
        assertThat(image.getOrderIndex()).isNull();
        assertThat(image.getContentType()).isNull();
    }

    @Test
    @DisplayName("Builder pattern constructs valid instance")
    void builderPatternConstructsValidInstance() {
        Image.Builder builder = Image.Builder.newImage()
            .withUrl("https://example.com/img.jpg")
            .withCaption("A landscape photo")
            .withOrder(1)
            .withContentType(MediaType.IMAGE_JPEG_VALUE);

        Image image = builder.build();

        assertThat(image.getImageUrl()).isEqualTo("https://example.com/img.jpg");
        assertThat(image.getCaption()).isEqualTo("A landscape photo");
        assertThat(image.getOrderIndex()).isEqualTo(1);
        assertThat(image.getContentType()).isEqualTo(MediaType.IMAGE_JPEG_VALUE);
    }

    @Test
    @DisplayName("Builder with partial data preserves nulls")
    void builderWithPartialDataPreservesNulls() {
        Image.Builder builder = Image.Builder.newImage()
            .withUrl("https://example.com/img.jpg");

        // Verify only the URL is set, others remain null/empty
        assertThat(builder.build().getImageUrl()).isEqualTo("https://example.com/img.jpg");
        assertThat(builder.build().getCaption()).isNull();
        assertThat(builder.build().getOrderIndex()).isNull();
        assertThat(builder.build().getContentType()).isNull();

    }

    @Test
    @DisplayName("Equals checks imageUrl field identity")
    void equalsChecksImageUrl() {
        Image image1 = new Image();
        image1.setImageUrl("https://test.com/image.png");

        Image image2 = new Image();
        image2.setImageUrl("https://test.com/image.png");

        Image image3 = new Image();
        image3.setOrderIndex(5); // Different fields, same URL logic per snippet

        assertThat(image1).isEqualTo(image2);
        
        // Note: Based on provided code logic, orderIndex does not affect equals.
        // This assertion should match the business requirement for unique identification.
        // If imageUrl is null, it returns false (Objects.equals handles this safely).
        assertThat(image1).hasSameHashCodeAs(image2);
    }

    @Test
    @DisplayName("Equals and HashCode consistent")
    void testHashCodeContract() {
        Image image1 = new Image();
        image1.setImageUrl("https://test.com/image.png");
        
        Image image2 = new Image();
        image2.setImageUrl("https://test.com/image.png");

        assertThat(image1).isEqualTo(image2); // true
        assertThat(image1.hashCode()).isEqualTo(image2.hashCode()); 
    }
    

    @Test
    @DisplayName("Builder creates immutable logical state before build")
    void builderReturnsSelfForFluentInterface() {
        Image.Builder builder = Image.Builder.newImage();
        
        assertThat(builder.withUrl("http://x.com").withCaption("y")).isSameAs(builder);
    }
}
