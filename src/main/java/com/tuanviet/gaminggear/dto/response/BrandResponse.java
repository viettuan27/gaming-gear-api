package com.tuanviet.gaminggear.dto.response;

public record BrandResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        boolean active
) {
}
