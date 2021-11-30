package com.pointOnSale.POS.configurations.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * S3Config class create a S3 client based on AWS configs available
 */
@Configuration
public class S3Config {
  @Value("${s3.region}")
  private String region;

  // Stored in secret manager
  @Value("${s3.access.key}")
  private String accessKey;

  //Stored in secret manager
  @Value("${s3.access.secret}")
  private String secretKey;

  @Bean
  public AmazonS3Client createAmazonS3Client() {
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(this.region).build();
  }
}
