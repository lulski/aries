package com.lulski.aries.aws.s3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.test.StepVerifier;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
class PresignedUrlControllerTest {

    @Mock
    private S3Presigner presigner;

    @Mock
    private PresignedGetObjectRequest presignedGetObjectRequest;

    private PresignedUrlController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PresignedUrlController(presigner);
    }

    @Test
    void testGeneratePresignedUrlSuccess() throws Exception {
        String bucket = "test-bucket";
        String object = "test-object";
        String expectedUrl = "https://s3.amazonaws.com/presigned-url";

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedGetObjectRequest);

        when(presignedGetObjectRequest.url())
                .thenReturn(new URI(expectedUrl).toURL());

        StepVerifier.create(controller.generatePresignedUrl(bucket, object))
                .assertNext(response -> {
                    assert response.getStatusCode().is2xxSuccessful();
                    assert response.getBody().equals(expectedUrl);
                })
                .verifyComplete();
    }

    @Test
    void testGeneratePresignedUrlWithSpecialCharacters() throws Exception {
        String bucket = "test-bucket";
        String object = "folder/test-object.txt";
        String expectedUrl = "https://s3.amazonaws.com/presigned-url";

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedGetObjectRequest);

        when(presignedGetObjectRequest.url()).thenReturn(new URI(expectedUrl).toURL());

        StepVerifier.create(controller.generatePresignedUrl(bucket, object))
                .assertNext(response -> {
                    assert response.getStatusCode().is2xxSuccessful();
                })
                .verifyComplete();
    }
}
