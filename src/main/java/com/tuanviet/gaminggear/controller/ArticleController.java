package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.ArticleRequest;
import com.tuanviet.gaminggear.dto.response.ArticleDetailResponse;
import com.tuanviet.gaminggear.dto.response.ArticleSummaryResponse;
import com.tuanviet.gaminggear.dto.response.PageResponse;
import com.tuanviet.gaminggear.entity.cms.ContentStatus;
import com.tuanviet.gaminggear.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Validated
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(value = "/api/v1/admin/cms/articles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> create(
            @Valid @ModelAttribute ArticleRequest request,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Tạo bài viết thành công", articleService.create(request, coverImage)));
    }

    @PutMapping(value = "/api/v1/admin/cms/articles/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute ArticleRequest request,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật bài viết thành công", articleService.update(id, request, coverImage)));
    }

    @PatchMapping("/api/v1/admin/cms/articles/{id}/publish")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> publish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Xuất bản bài viết thành công", articleService.publish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/articles/{id}/unpublish")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Gỡ xuất bản bài viết thành công", articleService.unpublish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/articles/{id}/archive")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> archive(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lưu trữ bài viết thành công", articleService.archive(id)));
    }

    @PatchMapping("/api/v1/admin/cms/articles/{id}/restore")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Khôi phục bài viết thành công", articleService.restore(id)));
    }

    @DeleteMapping("/api/v1/admin/cms/articles/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        articleService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa bài viết thành công", null));
    }

    @GetMapping("/api/v1/admin/cms/articles/{id}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy chi tiết bài viết thành công", articleService.getById(id)));
    }

    @GetMapping("/api/v1/admin/cms/articles")
    public ResponseEntity<ApiResponse<PageResponse<ArticleSummaryResponse>>> getAll(
            @RequestParam(required = false) ContentStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách bài viết thành công",
                        articleService.getAll(status, page, size, sortDirection)));
    }

    @GetMapping("/api/v1/articles/{slug}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> getPublishedBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy bài viết thành công",
                        articleService.getPublishedBySlug(slug)));
    }

    @GetMapping("/api/v1/articles")
    public ResponseEntity<ApiResponse<PageResponse<ArticleSummaryResponse>>> getAllPublished(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách bài viết thành công",
                        articleService.getAllPublished(page, size, sortDirection)));
    }
}