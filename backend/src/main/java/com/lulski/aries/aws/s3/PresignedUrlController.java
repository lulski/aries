package com.lulski.aries.aws.s3;

import java.time.Duration;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    public PresignedUrlController(S3Presigner presigner) {
        this.presigner = presigner;
    }

    @GetMapping("/s3/presigned-url/{bucket}/{object}")
    public Mono<ResponseEntity<PresignedUrlResponseDto>> generatePresignedUrl(
            @PathVariable String bucket,
            @PathVariable String object) {

        return Mono.fromCallable(() -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(object)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                    .builder().signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            String url = presigner.presignGetObject(presignRequest).url().toString();

            return ResponseEntity.ok(new PresignedUrlResponseDto(url));
        }).subscribeOn(Schedulers.boundedElastic());// move blocking work off main thread
    }

    /**
     * Generate a presigned PUT URL for uploading a file to the given bucket/key.
     * Example: GET /s3/presign/aries-springboot-jar/aries_jar
     */
    @GetMapping("/presign/{bucket}/{object:.+}")
    public Mono<ResponseEntity<PresignedUrlDto>> presignPut(
            @PathVariable String bucket,
            @PathVariable("object") String object) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(object)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);

        long expiresInSeconds = Math.max(0,
                Duration.between(Instant.now(), presigned.expiration()).getSeconds());

        PresignedUrlDto dto = new PresignedUrlDto(presigned.url().toString(), "PUT", expiresInSeconds);

        return Mono.just(ResponseEntity.ok(dto));
    }

    public record PresignedUrlDto(String url, String method, long expiresInSeconds) {
    }

}
