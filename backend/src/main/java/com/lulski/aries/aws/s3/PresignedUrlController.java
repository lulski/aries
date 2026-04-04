package com.lulski.aries.aws.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@RestController
public class PresignedUrlController {

    private final S3Presigner presigner;

    private static final Logger logger = LoggerFactory.getLogger(PresignedUrlController.class);

    public PresignedUrlController(S3Presigner presigner) {
        this.presigner = presigner;
    }

    /**
     * generates URL that allows access to the supplied bucketName and postId
     * @param bucketName the name of the S3 bucket
     * @param postId the ID of the post
     * @return a Mono containing the presigned URL
     */
    @GetMapping("/s3/presigned-url/{bucketName}/{postId}")
    public Mono<ResponseEntity<String>> generatePresignedUrl(
            @PathVariable String bucketName,
            @PathVariable String postId) {

        return Mono.fromCallable(() -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(postId)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                    .builder().signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            String url = presigner.presignGetObject(presignRequest).url().toString();

            return ResponseEntity.ok(url);
        }).subscribeOn(Schedulers.boundedElastic());// move blocking work off main thread
    }

    /**
     * generates URL that allows access to the supplied bucketName and postId
     * @param bucketName the name of the S3 bucket
     * @param postId the ID of the post
     * @return a Mono containing the presigned URL
     */    
    @PostMapping("/s3/presigned-url/{bucketName}/{postId}")
    public Mono<String> createPresignedUrl(
            @PathVariable String bucketName, 
            @PathVariable String postId,
            Map<String, String> metadata) {

        // Validate inputs first
        if (!validateInput(bucketName, postId)) {
            return Mono.error(new IllegalArgumentException("Invalid bucket/postId names"));
        }

        return Mono.fromCallable(() -> {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(postId)
                    .metadata(metadata)
                    .contentType(getContentTypeFromMetadata(metadata))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            return presigner.presignPutObject(presignRequest).url().toExternalForm();
        })
        .subscribeOn(Schedulers.boundedElastic()) // Critical!
        .doOnError(e -> {
            logger.error("Failed to create presigned URL", e);
             }
            )
        .onErrorResume(e -> Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(e.getMessage())
                        .toString()
        ));
    }

    private boolean validateInput(String bucket, String object) {
        return !bucket.isEmpty() && !bucket.contains("..") && 
               !object.isEmpty() && !object.contains("..");
    }

    private String getContentTypeFromMetadata(Map<String, String> metadata) {
        return metadata.getOrDefault("content-type", "application/octet-stream");
    }

}
