package com.tuanviet.gaminggear.dto.response;

public record ProductImageResponse(
        Long id,
        Long productId,
        String imageUrl,
        int sortOrder
) {
}
