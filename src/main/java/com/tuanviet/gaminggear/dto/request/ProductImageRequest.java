package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductImageRequest(

        @NotBlank(message = "Đường dẫn ảnh không được để trống")
        @Size(max = 500, message = "Đường dẫn ảnh tối đa 500 ký tự")
        String imageUrl,

        @Min(value = 0, message = "Thứ tự ảnh không được âm")
        int sortOrder
) {
}
