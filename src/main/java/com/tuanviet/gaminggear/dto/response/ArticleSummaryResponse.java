package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.cms.ContentStatus;

import java.time.Instant;

public record ArticleSummaryResponse(
        Long id,
        String title,
        String slug,
        String excerpt,
        String coverImageUrl,
        ContentStatus status,
        Instant publishedAt
) {
}
