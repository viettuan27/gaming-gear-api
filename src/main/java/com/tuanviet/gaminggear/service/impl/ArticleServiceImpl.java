package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.ArticleRequest;
import com.tuanviet.gaminggear.dto.response.ArticleDetailResponse;
import com.tuanviet.gaminggear.dto.response.ArticleSummaryResponse;
import com.tuanviet.gaminggear.dto.response.PageResponse;
import com.tuanviet.gaminggear.entity.cms.Article;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.exception.ConflictException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.ArticleMapper;
import com.tuanviet.gaminggear.repository.ArticleRepository;
import com.tuanviet.gaminggear.service.ArticleService;
import com.tuanviet.gaminggear.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final FileStorageService fileStorageService;

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse create(ArticleRequest request, MultipartFile coverImage) {
        String slug = normalizeSlug(request.slug());

        if (articleRepository.existsBySlugIgnoreCase(slug)) {
            throw new ConflictException("Slug bài viết đã tồn tại");
        }

        Article article = new Article();
        updateArticleFields(article, request, slug);

        if (coverImage == null) {
            return articleMapper.toDetailResponse(articleRepository.save(article));
        }

        String objectKey = fileStorageService.uploadImage("cms/articles", coverImage);

        try {
            article.setCoverImageObjectKey(objectKey);

            registerDeleteOnRollback(objectKey);

            return articleMapper.toDetailResponse(articleRepository.save(article));

        } catch (RuntimeException exception) {
            fileStorageService.delete(objectKey);
            throw exception;
        }
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse update(Long id, ArticleRequest request, MultipartFile coverImage) {
        Article article = getArticleById(id);
        String slug = normalizeSlug(request.slug());

        if (articleRepository.existsBySlugIgnoreCaseAndIdNot(slug, id)) {
            throw new ConflictException("Slug bài viết đã tồn tại");
        }

        updateArticleFields(article, request, slug);

        if (coverImage == null) {
            return articleMapper.toDetailResponse(articleRepository.save(article));
        }

        String oldObjectKey = article.getCoverImageObjectKey();
        String newObjectKey = fileStorageService.uploadImage("cms/articles", coverImage
        );

        try {
            article.setCoverImageObjectKey(newObjectKey);

            registerDeleteOnRollback(newObjectKey);
            registerDeleteAfterCommit(oldObjectKey);

            return articleMapper.toDetailResponse(articleRepository.save(article));

        } catch (RuntimeException exception) {
            fileStorageService.delete(newObjectKey);
            throw exception;
        }
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse publish(Long id) {
        Article article = getArticleById(id);

        if (article.getStatus() != ContentStatus.DRAFT) {
            throw new BadRequestException(
                    "Chỉ có thể xuất bản bài viết đang ở trạng thái nháp");
        }

        article.setStatus(ContentStatus.PUBLISHED);
        article.setPublishedAt(Instant.now());

        return articleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse unpublish(Long id) {
        Article article = getArticleById(id);

        if (article.getStatus() != ContentStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Chỉ có thể gỡ xuất bản bài viết đang được xuất bản");
        }

        article.setStatus(ContentStatus.DRAFT);
        article.setPublishedAt(null);

        return articleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse archive(Long id) {
        Article article = getArticleById(id);

        if (article.getStatus() == ContentStatus.ARCHIVED) {
            throw new BadRequestException("Bài viết đã được lưu trữ");
        }

        article.setStatus(ContentStatus.ARCHIVED);
        article.setPublishedAt(null);

        return articleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public ArticleDetailResponse restore(Long id) {
        Article article = getArticleById(id);

        if (article.getStatus() != ContentStatus.ARCHIVED) {
            throw new BadRequestException(
                    "Chỉ có thể khôi phục bài viết đã được lưu trữ");
        }

        article.setStatus(ContentStatus.DRAFT);
        article.setPublishedAt(null);

        return articleMapper.toDetailResponse(articleRepository.save(article));
    }

    @Override
    @CacheEvict(cacheNames = "cms-article-detail", allEntries = true)
    public void delete(Long id) {
        Article article = getArticleById(id);

        if (article.getStatus() == ContentStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Không thể xóa bài viết đang được xuất bản. "
                            + "Hãy gỡ xuất bản hoặc lưu trữ bài viết trước");
        }

        articleRepository.delete(article);
        registerDeleteAfterCommit(article.getCoverImageObjectKey());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleDetailResponse getById(Long id) {
        return articleMapper.toDetailResponse(getArticleById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ArticleSummaryResponse> getAll(
            ContentStatus status,
            int page,
            int size,
            String sortDirection) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                createSort("createdAt", sortDirection));

        Page<Article> articlePage = status == null
                ? articleRepository.findAll(pageable)
                : articleRepository.findByStatus(status, pageable);

        return toPageResponse(articlePage);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "cms-article-detail",
            key = "#slug.trim().toLowerCase()"
    )
    public ArticleDetailResponse getPublishedBySlug(String slug) {
        String normalizedSlug = normalizeSlug(slug);

        return articleRepository
                .findBySlugIgnoreCaseAndStatus(normalizedSlug, ContentStatus.PUBLISHED)
                .map(articleMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy bài viết"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ArticleSummaryResponse> getAllPublished(
            int page,
            int size,
            String sortDirection) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                createSort("publishedAt", sortDirection));

        Page<Article> articlePage = articleRepository.findByStatus(
                ContentStatus.PUBLISHED,
                pageable);

        return toPageResponse(articlePage);
    }

    private Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));
    }

    private void updateArticleFields(
            Article article,
            ArticleRequest request,
            String slug
    ) {
        article.setTitle(request.title().trim());
        article.setSlug(slug);
        article.setExcerpt(normalizeOptionalText(request.excerpt()));
        article.setContent(request.content());
        article.setMetaTitle(normalizeOptionalText(request.metaTitle()));
        article.setMetaDescription(normalizeOptionalText(request.metaDescription()));
    }

    private PageResponse<ArticleSummaryResponse> toPageResponse(
            Page<Article> articlePage
    ) {
        Page<ArticleSummaryResponse> responsePage = articlePage.map(articleMapper::toSummaryResponse);

        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        );
    }

    private Sort createSort(String property, String sortDirection) {
        if (!"asc".equalsIgnoreCase(sortDirection)
                && !"desc".equalsIgnoreCase(sortDirection)) {
            throw new BadRequestException("Chiều sắp xếp chỉ có thể là asc hoặc desc");
        }

        return sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(property).ascending()
                : Sort.by(property).descending();
    }

    private String normalizeSlug(String slug) {
        return slug.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private void registerDeleteAfterCommit(String objectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        fileStorageService.delete(objectKey);
                    }
                }
        );
    }

    private void registerDeleteOnRollback(String objectKey) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) {
                            fileStorageService.delete(objectKey);
                        }
                    }
                }
        );
    }
}