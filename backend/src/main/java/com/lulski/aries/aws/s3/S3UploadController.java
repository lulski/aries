package com.lulski.aries.aws.s3;

import java.nio.ByteBuffer;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
public class S3UploadController {

    private final S3AsyncClient s3AsyncClient;

    public S3UploadController(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
    }

    @PutMapping(value = "/s3/upload/{bucket}/{object}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<UploadResponseDto>> uploadToS3(
            @PathVariable String bucket,
            @PathVariable String object,
            @RequestPart("file") FilePart filePart) {

        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    try {
                        ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);

                        PutObjectRequest putReq = PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(object)
                                .contentLength((long) bytes.length)
                                .build();

                        return Mono.fromFuture(
                                s3AsyncClient.putObject(putReq, AsyncRequestBody.fromBytes(bytes)))
                                .doFinally(signal -> DataBufferUtils.release(dataBuffer))
                                .map(putResp -> ResponseEntity.ok(new UploadResponseDto("OK", object)));
                    } catch (Exception e) {
                        DataBufferUtils.release(dataBuffer);
                        return Mono.error(e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public static class UploadResponseDto {
        private final String status;
        private final String key;

        public UploadResponseDto(String status, String key) {
            this.status = status;
            this.key = key;
        }

        public String getStatus() {
            return status;
        }

        public String getKey() {
            return key;
        }
    }
}