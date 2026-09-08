package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.cms.ContentStatus;

import java.time.Instant;

public record StaticPageResponse(
        Long id,
        String title,
        String slug,
        String content,
        String metaTitle,
        String metaDescription,
        ContentStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
