package com.lulski.aries.aws.s3;

import java.time.Instant;

public record PresignedUrlResponseDto(
        String url,
        Instant expiresAt,
        String bucketName,
        String fileName,
        String objectSizeUnit
) {


}
