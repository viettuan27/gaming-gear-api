package com.tuanviet.gaminggear.dto.request;

import com.tuanviet.gaminggear.entity.cms.BannerPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BannerRequest(
        @NotBlank(message = "Tiêu đề banner không được để trống")
        @Size(max = 150, message = "Tiêu đề banner tối đa 150 ký tự")
        String title,

        @Size(max = 500, message = "Đường dẫn điều hướng tối đa 500 ký tự")
        String redirectUrl,

        @NotNull(message = "Vị trí banner không được để trống")
        BannerPosition position,

        @NotNull(message = "Thứ tự hiển thị không được để trống")
        @PositiveOrZero(message = "Thứ tự hiển thị không được âm")
        Integer sortOrder
) {
}