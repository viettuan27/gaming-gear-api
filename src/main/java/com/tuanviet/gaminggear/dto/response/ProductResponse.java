package com.tuanviet.gaminggear.dto.response;

public record ProductResponse(
        Long id,
        Long categoryId,
        String categoryName,
        Long brandId,
        String brandName,
        String name,
        String description,
        boolean active
) {
}
