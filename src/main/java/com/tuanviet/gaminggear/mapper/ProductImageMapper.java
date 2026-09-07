package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import com.tuanviet.gaminggear.entity.catalog.ProductImage;
import com.tuanviet.gaminggear.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final FileStorageService fileStorageService;

    public ProductImageResponse toResponse(ProductImage productImage) {
        return new ProductImageResponse(
                productImage.getId(),
                productImage.getProduct().getId(),
                fileStorageService.getPublicUrl(productImage.getObjectKey()),
                productImage.getSortOrder()
        );
    }
}
