package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.BannerRequest;
import com.tuanviet.gaminggear.dto.response.BannerResponse;
import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {

    BannerResponse create(BannerRequest request, MultipartFile file);

    BannerResponse update(Long id, BannerRequest request, MultipartFile file);

    BannerResponse publish(Long id);

    BannerResponse unpublish(Long id);

    BannerResponse archive(Long id);

    BannerResponse restore(Long id);

    void delete(Long id);

    BannerResponse getById(Long id);

    List<BannerResponse> getAll();

    List<BannerResponse> getPublishedByPosition(BannerPosition position);
}