package com.lulski.aries.aws.s3;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;



@RestController
public class PresignedUrlController {

    private final S3Presigner presigner;
    private final Logger LOGGER = LoggerFactory.getLogger(PresignedUrlController.class);

    @Value("${aries.fileupload.s3.duration.timeout.minutes}")
    int DURATION_TIMEOUT;


    public PresignedUrlController(S3Presigner presigner) {
        this.presigner = presigner;
    }

    /**
     * generates URL that allows View access to the supplied bucketName and fileName
     *
     * @param bucketName the name of the S3 bucket
     * @param fileName   the name of the file in the S3 bucket
     * @return a Mono containing the presigned URL
     */
    @GetMapping("/s3/presigned/{bucketName}/{fileName}")
    public Mono<ResponseEntity<String>> generatePresignedUrl(
            @PathVariable String bucketName,
            @PathVariable String fileName) {

        return Mono.fromCallable(() -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                    .builder().signatureDuration(Duration.ofMinutes(DURATION_TIMEOUT))
                    .getObjectRequest(getObjectRequest)
                    .build();

            String url = presigner.presignGetObject(presignRequest).url().toString();

            return ResponseEntity.ok(url);
        }).subscribeOn(Schedulers.boundedElastic());// move blocking work off main thread
    }

    /**
     * generates URL that allows upload access to the supplied bucketName and
     * fileName
     * this is so the frontend app can upload the image directly to S3 without going
     * through the backend, which is more efficient and scalable
     *
     * @param bucketName the name of the S3 bucket
     * @param fileName   the name of the file in the S3 bucket
     * @return a Mono containing the presigned URL
     */
    @PostMapping("/s3/presigned/{bucketName}")
    public Mono<PresignedUrlResponseDto> createPresignedUrl(
            @PathVariable String bucketName,
            @RequestBody(required = true) Map<String, String> metadata) {

        if (!validateInput(bucketName)) {
            return Mono.error(new IllegalArgumentException("Invalid bucket or file name"));
        }

        var fileName = metadata.get("name");
        var contentType = metadata.get("type");
        var fileSize = metadata.get("size");

        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            LOGGER.info("Metadata key: {}, value: {}", entry.getKey(), entry.getValue());
        }

        LOGGER.info("Received request to create presigned URL for bucket: {}, fileName: {}, contentType: {}",
                bucketName, fileName, contentType);


        return Mono.fromCallable(() -> {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .metadata(metadata)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(DURATION_TIMEOUT))
                    .putObjectRequest(objectRequest)
                    .build();

            var presignResponse = presigner.presignPutObject(presignRequest);

                    
            var presignedUrlResponse = new PresignedUrlResponseDto(
                presignResponse.url().toExternalForm(),
                Instant.now().plusSeconds(DURATION_TIMEOUT * 60),
                bucketName,
                fileName,
                fileSize
                );

            return presignedUrlResponse;
        })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(e -> new PresignedUrlException("Failed to create presigned URL", e));
    }

    private boolean validateInput(String bucket) {
        return !bucket.isEmpty() && !bucket.contains("..");
    }


    private boolean validateMIMEType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return false;
        }
        return contentType.startsWith("image/");
    }

}
