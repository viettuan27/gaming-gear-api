package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.BannerRequest;
import com.tuanviet.gaminggear.dto.response.BannerResponse;
import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import com.tuanviet.gaminggear.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class BannerController {

    private final BannerService bannerService;

    @PostMapping(value = "/api/v1/admin/cms/banners", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BannerResponse>> create(
            @Valid @ModelAttribute BannerRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo banner thành công", bannerService.create(request, file)));
    }

    @PutMapping(value = "/api/v1/admin/cms/banners/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BannerResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute BannerRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật banner thành công", bannerService.update(id, request, file)));
    }

    @PatchMapping("/api/v1/admin/cms/banners/{id}/publish")
    public ResponseEntity<ApiResponse<BannerResponse>> publish(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Xuất bản banner thành công", bannerService.publish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/banners/{id}/unpublish")
    public ResponseEntity<ApiResponse<BannerResponse>> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Gỡ xuất bản banner thành công", bannerService.unpublish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/banners/{id}/archive")
    public ResponseEntity<ApiResponse<BannerResponse>> archive(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lưu trữ banner thành công", bannerService.archive(id)));
    }

    @PatchMapping("/api/v1/admin/cms/banners/{id}/restore")
    public ResponseEntity<ApiResponse<BannerResponse>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Khôi phục banner thành công", bannerService.restore(id)));
    }

    @DeleteMapping("/api/v1/admin/cms/banners/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bannerService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa banner thành công", null)
        );
    }

    @GetMapping("/api/v1/admin/cms/banners/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy chi tiết banner thành công", bannerService.getById(id)));
    }

    @GetMapping("/api/v1/admin/cms/banners")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách banner thành công", bannerService.getAll()));
    }

    @GetMapping("/api/v1/banners")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getPublishedByPosition(@RequestParam BannerPosition position) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách banner thành công",
                        bannerService.getPublishedByPosition(position)));
    }
}