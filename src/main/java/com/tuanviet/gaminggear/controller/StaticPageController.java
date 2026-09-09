package com.tuanviet.gaminggear.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "CMS",
        description = "Quản lý banner, trang nội dung và bài viết"
)
@RestController
@RequiredArgsConstructor
public class StaticPageController {

    private final StaticPageService staticPageService;

    @Operation(summary = "Tạo trang nội dung mới")
    @PostMapping("/api/v1/admin/cms/pages")
    public ResponseEntity<ApiResponse<StaticPageResponse>> create(@Valid @RequestBody StaticPageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Tạo trang nội dung thành công", staticPageService.create(request)));
    }

    @Operation(summary = "Cập nhật trang nội dung")
    @PutMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StaticPageRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật trang nội dung thành công", staticPageService.update(id, request)));
    }

    @Operation(summary = "Xuất bản trang nội dung")
    @PatchMapping("/api/v1/admin/cms/pages/{id}/publish")
    public ResponseEntity<ApiResponse<StaticPageResponse>> publish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Xuất bản trang nội dung thành công", staticPageService.publish(id)));
    }

    @Operation(summary = "Gỡ xuất bản trang nội dung")
    @PatchMapping("/api/v1/admin/cms/pages/{id}/unpublish")
    public ResponseEntity<ApiResponse<StaticPageResponse>> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Gỡ xuất bản trang nội dung thành công", staticPageService.unpublish(id)));
    }

    @Operation(summary = "Lưu trữ trang nội dung")
    @PatchMapping("/api/v1/admin/cms/pages/{id}/archive")
    public ResponseEntity<ApiResponse<StaticPageResponse>> archive(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lưu trữ trang nội dung thành công", staticPageService.archive(id)));
    }

    @Operation(summary = "Khôi phục trang nội dung")
    @PatchMapping("/api/v1/admin/cms/pages/{id}/restore")
    public ResponseEntity<ApiResponse<StaticPageResponse>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Khôi phục trang nội dung thành công", staticPageService.restore(id)));
    }

    @Operation(summary = "Xóa trang nội dung")
    @DeleteMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        staticPageService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa trang nội dung thành công", null));
    }

    @Operation(summary = "Lấy chi tiết trang nội dung cho quản trị viên")
    @GetMapping("/api/v1/admin/cms/pages/{id}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy chi tiết trang nội dung thành công", staticPageService.getById(id)));
    }

    @Operation(summary = "Lấy toàn bộ trang nội dung cho quản trị viên")
    @GetMapping("/api/v1/admin/cms/pages")
    public ResponseEntity<ApiResponse<List<StaticPageResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách trang nội dung thành công", staticPageService.getAll()));
    }

    @Operation(summary = "Lấy danh sách trang nội dung đã xuất bản")
    @GetMapping("/api/v1/pages")
    public ResponseEntity<ApiResponse<List<StaticPageResponse>>> getPublishedPages() {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách trang nội dung thành công", staticPageService.getPublishedPages()));
    }

    @Operation(summary = "Lấy trang nội dung đã xuất bản theo slug")
    @GetMapping("/api/v1/pages/{slug}")
    public ResponseEntity<ApiResponse<StaticPageResponse>> getPublishedBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy trang nội dung thành công", staticPageService.getPublishedBySlug(slug)));
    }
}
