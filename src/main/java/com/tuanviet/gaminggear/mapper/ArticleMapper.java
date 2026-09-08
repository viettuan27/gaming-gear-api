package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.ArticleDetailResponse;
import com.tuanviet.gaminggear.dto.response.ArticleSummaryResponse;
import com.tuanviet.gaminggear.entity.cms.Article;
import com.tuanviet.gaminggear.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleMapper {

    private final FileStorageService fileStorageService;

    public ArticleSummaryResponse toSummaryResponse(Article article){
        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getExcerpt(),
                getCoverImageUrl(article),
                article.getStatus(),
                article.getPublishedAt()
        );
    }

    public ArticleDetailResponse toDetailResponse(Article article){
        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getExcerpt(),
                article.getContent(),
                getCoverImageUrl(article),
                article.getMetaTitle(),
                article.getMetaDescription(),
                article.getStatus(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    private String getCoverImageUrl(Article article){
        if(article.getCoverImageObjectKey() == null
                || article.getCoverImageObjectKey().isBlank()){
            return null;
        }
        return fileStorageService.getPublicUrl(article.getCoverImageObjectKey());
    }
}
