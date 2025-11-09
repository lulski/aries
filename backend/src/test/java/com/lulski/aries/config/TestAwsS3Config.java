package com.lulski.aries.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@TestConfiguration
public class TestAwsS3Config {
    @Bean
    public S3Presigner s3Presigner() {
        return Mockito.mock(S3Presigner.class);
    }

}