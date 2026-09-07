package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileStorageService implements FileStorageService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    private static final Map<String, String> IMAGE_EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.public-url}")
    private String publicUrl;

    @Override
    public String uploadImage(String folder, MultipartFile file) {
        String extension = validateImage(file);

        String objectKey = folder + "/" + UUID.randomUUID()
                + extension;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(inputStream, file.getSize(), -1L)
                            .contentType(file.getContentType())
                            .build()
            );

            return objectKey;
        } catch (Exception exception) {
            throw new IllegalStateException("Không thể tải ảnh lên MinIO", exception);
        }
    }

    @Override
    public void delete(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return;
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
        } catch (Exception exception) {
            log.error("Không thể xóa object MinIO: {}", objectKey, exception);
        }
    }

    @Override
    public String getPublicUrl(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return null;
        }

        String normalizedPublicUrl = publicUrl.endsWith("/")
                ? publicUrl.substring(0, publicUrl.length() - 1)
                : publicUrl;

        return normalizedPublicUrl + "/" + bucket + "/" + objectKey;
    }

    private String validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Ảnh không được để trống");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException("Kích thước ảnh tối đa là 5 MB");
        }

        String extension = IMAGE_EXTENSIONS.get(file.getContentType());
        if (extension != null) {
            return extension;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Chỉ hỗ trợ ảnh JPEG, PNG hoặc WebP");
        }

        String lowerCaseFilename = originalFilename.toLowerCase(Locale.ROOT);
        if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return ".jpg";
        }

        if (lowerCaseFilename.endsWith(".png")) {
            return ".png";
        }

        if (lowerCaseFilename.endsWith(".webp")) {
            return ".webp";
        }

        throw new BadRequestException("Chỉ hỗ trợ ảnh JPEG, PNG hoặc WebP");
    }
}
