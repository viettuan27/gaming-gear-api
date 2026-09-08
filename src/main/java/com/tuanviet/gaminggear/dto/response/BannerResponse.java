package com.tuanviet.gaminggear.dto.response;

import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;

import java.time.Instant;

public record BannerResponse(
        Long id,
        String title,
        String imageUrl,
        String redirectUrl,
        BannerPosition position,
        int sortOrder,
        ContentStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
