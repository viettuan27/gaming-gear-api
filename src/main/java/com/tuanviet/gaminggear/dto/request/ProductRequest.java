package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotNull(message = "Danh mục không được để trống")
        Long categoryId,
        @NotNull(message = "Thương hiệu không được để trống")
        Long brandId,
        @NotBlank(message = "Tên sản phẩm không được để trống")
        @Size(max = 150, message = "Tên sản phẩm tối đa 150 ký tự")
        String name,
        String description,
        boolean active
) {
}
