package com.tuanviet.gaminggear.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioBucketInitializer {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @PostConstruct
    public void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );

                log.info("Đã tạo MinIO bucket: {}", bucket);
            }

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucket)
                            .config(getPublicReadPolicy())
                            .build()
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Không thể khởi tạo MinIO bucket", exception);
        }
    }

    private String getPublicReadPolicy() {
        return """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": {
                        "AWS": ["*"]
                      },
                      "Action": ["s3:GetObject"],
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(bucket);
    }
}
