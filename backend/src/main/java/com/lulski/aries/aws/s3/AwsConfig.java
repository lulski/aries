
package com.lulski.aries.aws.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {
    @Value("${aws.region}")
    String region;

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder().region(Region.AP_SOUTHEAST_2).build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder().build();

        return S3AsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .httpClient(httpClient)
                .build();
    }

}