package com.lulski.aries.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class TestAwsS3Config {
    @Bean
    public S3Presigner s3Presigner() {
        return Mockito.mock(S3Presigner.class);
    }
}