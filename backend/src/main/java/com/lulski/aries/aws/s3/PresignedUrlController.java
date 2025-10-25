package com.lulski.aries.aws.s3;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@RestController
public class PresignedUrlController {

    private final S3Presigner presigner;

    public PresignedUrlController(S3Presigner presigner) {
        this.presigner = presigner;
    }

    @GetMapping("/s3/presigned-url/{bucket}/{object}")
    public Mono<ResponseEntity<String>> generatePresignedUrl(
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

            return ResponseEntity.ok(url);
        }).subscribeOn(Schedulers.boundedElastic());// move blocking work off main thread
    }

}
