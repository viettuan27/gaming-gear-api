package com.tuanviet.gaminggear.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import com.tuanviet.gaminggear.service.ProductImageService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Danh mục sản phẩm",
        description = "Quản lý hình ảnh sản phẩm"
)
@RestController
@RequiredArgsConstructor
@Validated
public class ProductImageController {

    private final ProductImageService productImageService;

    @Operation(summary = "Tải ảnh cho sản phẩm")
    @PostMapping(
            value = "/api/v1/admin/products/{productId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<ProductImageResponse>> create(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("sortOrder")
            @Min(value = 0, message = "Thứ tự ảnh không được âm")
            int sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Thêm ảnh sản phẩm thành công",
                        productImageService.create(productId, file, sortOrder)
                )
        );
    }

    @Operation(summary = "Cập nhật ảnh sản phẩm")
    @PutMapping(
            value = "/api/v1/admin/images/{imageId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<ProductImageResponse>> update(
            @PathVariable Long imageId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("sortOrder")
            @Min(value = 0, message = "Thứ tự ảnh không được âm")
            int sortOrder
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật ảnh sản phẩm thành công",
                        productImageService.update(imageId, file, sortOrder)
                )
        );
    }

    @Operation(summary = "Xóa ảnh sản phẩm")
    @DeleteMapping("/api/v1/admin/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long imageId) {
        productImageService.delete(imageId);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa ảnh sản phẩm thành công", null)
        );
    }
}
