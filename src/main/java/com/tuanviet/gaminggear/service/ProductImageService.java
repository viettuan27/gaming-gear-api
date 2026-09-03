package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.ProductImageRequest;
import com.tuanviet.gaminggear.dto.response.ProductImageResponse;

public interface ProductImageService {

    ProductImageResponse create(Long productId, ProductImageRequest request);

    ProductImageResponse update(Long imageId, ProductImageRequest request);

    void delete(Long imageId);
}
