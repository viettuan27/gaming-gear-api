package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.BannerResponse;
import com.tuanviet.gaminggear.entity.cms.Banner;
import com.tuanviet.gaminggear.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BannerMapper {
    private final FileStorageService fileStorageService;

    public BannerResponse toResponse(Banner banner){
        return new BannerResponse(
                banner.getId(),
                banner.getTitle(),
                fileStorageService.getPublicUrl(banner.getImageObjectKey()),
                banner.getRedirectUrl(),
                banner.getPosition(),
                banner.getSortOrder(),
                banner.getStatus(),
                banner.getCreatedAt(),
                banner.getUpdatedAt()
        );
    }
}
