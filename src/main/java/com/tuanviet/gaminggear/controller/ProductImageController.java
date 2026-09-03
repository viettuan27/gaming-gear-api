package com.tuanviet.gaminggear.controller;

import com.tuanviet.gaminggear.common.ApiResponse;
import com.tuanviet.gaminggear.dto.request.ProductImageRequest;
import com.tuanviet.gaminggear.dto.response.ProductImageResponse;
import com.tuanviet.gaminggear.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/api/v1/admin/products/{productId}/images")
    public ResponseEntity<ApiResponse<ProductImageResponse>> create(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Thêm ảnh sản phẩm thành công",
                        productImageService.create(productId, request)
                )
        );
    }

    @PutMapping("/api/v1/admin/images/{imageId}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> update(
            @PathVariable Long imageId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật ảnh sản phẩm thành công",
                        productImageService.update(imageId, request)
                )
        );
    }

    @DeleteMapping("/api/v1/admin/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long imageId) {
        productImageService.delete(imageId);

        return ResponseEntity.ok(
                ApiResponse.success("Xóa ảnh sản phẩm thành công", null)
        );
    }
}
