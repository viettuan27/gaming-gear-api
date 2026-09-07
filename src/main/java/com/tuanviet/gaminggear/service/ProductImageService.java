package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    ProductImageResponse create(Long productId, MultipartFile file, int sortOrder);

    ProductImageResponse update(Long imageId, MultipartFile file, int sortOrder);

    void delete(Long imageId);
}
