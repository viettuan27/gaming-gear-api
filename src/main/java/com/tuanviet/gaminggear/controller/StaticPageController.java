package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.StaticPageRequest;
import com.tuanviet.gaminggear.dto.response.StaticPageResponse;
import com.tuanviet.gaminggear.service.StaticPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StaticPageController {

    private final StaticPageService staticPageService;

    @PostMapping("/api/v1/admin/cms/pages")
    public ResponseEntity<ApiResponse<StaticPageResponse>> create(@Valid @RequestBody StaticPageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Tạo trang nội dung thành công", staticPageService.create(request)));
    }

    @PutMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StaticPageRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật trang nội dung thành công", staticPageService.update(id, request)));
    }

    @PatchMapping("/api/v1/admin/cms/pages/{id}/publish")
    public ResponseEntity<ApiResponse<StaticPageResponse>> publish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Xuất bản trang nội dung thành công", staticPageService.publish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/pages/{id}/unpublish")
    public ResponseEntity<ApiResponse<StaticPageResponse>> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Gỡ xuất bản trang nội dung thành công", staticPageService.unpublish(id)));
    }

    @PatchMapping("/api/v1/admin/cms/pages/{id}/archive")
    public ResponseEntity<ApiResponse<StaticPageResponse>> archive(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lưu trữ trang nội dung thành công", staticPageService.archive(id)));
    }

    @PatchMapping("/api/v1/admin/cms/pages/{id}/restore")
    public ResponseEntity<ApiResponse<StaticPageResponse>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Khôi phục trang nội dung thành công", staticPageService.restore(id)));
    }

    @DeleteMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        staticPageService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa trang nội dung thành công", null));
    }

    @GetMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy chi tiết trang nội dung thành công", staticPageService.getById(id)));
    }

    @GetMapping("/api/v1/admin/cms/pages")
    public ResponseEntity<ApiResponse<List<StaticPageResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách trang nội dung thành công", staticPageService.getAll()));
    }

    @GetMapping("/api/v1/pages/{slug}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> getPublishedBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy trang nội dung thành công", staticPageService.getPublishedBySlug(slug)));
    }
}