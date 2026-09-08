package com.tuanviet.gaminggear.service;

import com.tuanviet.gaminggear.dto.request.ArticleRequest;
import com.tuanviet.gaminggear.dto.response.ArticleDetailResponse;
import com.tuanviet.gaminggear.dto.response.ArticleSummaryResponse;
import com.tuanviet.gaminggear.dto.response.PageResponse;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    ArticleDetailResponse create(ArticleRequest request, MultipartFile coverImage);

    ArticleDetailResponse update(Long id, ArticleRequest request, MultipartFile coverImage);

    ArticleDetailResponse publish(Long id);

    ArticleDetailResponse unpublish(Long id);

    ArticleDetailResponse archive(Long id);

    ArticleDetailResponse restore(Long id);

    void delete(Long id);

    ArticleDetailResponse getById(Long id);

    PageResponse<ArticleSummaryResponse> getAll(
            ContentStatus status,
            int page,
            int size,
            String sortDirection
    );

    ArticleDetailResponse getPublishedBySlug(String slug);

    PageResponse<ArticleSummaryResponse> getAllPublished(
            int page,
            int size,
            String sortDirection
    );
}