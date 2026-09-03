package com.tuanviet.gaminggear.dto.response;

import java.util.List;

public record ProductDetailsResponse(
        Long id,
        Long categoryId,
        String categoryName,
        Long brandId,
        String brandName,
        String name,
        String description,
        List<ProductVariantResponse> variants,
        List<ProductImageResponse> images
) {
}
