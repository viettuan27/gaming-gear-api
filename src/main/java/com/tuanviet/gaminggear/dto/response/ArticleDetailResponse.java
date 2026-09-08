package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.cms.ContentStatus;

import java.time.Instant;

public record ArticleDetailResponse(
        Long id,
        String title,
        String slug,
        String excerpt,
        String content,
        String coverImageUrl,
        String metaTitle,
        String metaDescription,
        ContentStatus status,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
