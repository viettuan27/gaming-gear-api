package com.tuanviet.gaminggear.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadImage(String folder, MultipartFile file);

    void delete(String objectKey);

    String getPublicUrl(String objectKey);
}
