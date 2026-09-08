package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StaticPageRequest(
        @NotBlank(message = "Tiêu đề trang không được để trống")
        @Size(max = 150, message = "Tiêu đề trang tối đa 150 ký tự")
        String title,

        @NotBlank(message = "Slug không được để trống")
        @Size(max = 200, message = "Slug tối đa 200 ký tự")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug chỉ gồm chữ thường, số và dấu gạch ngang"
        )
        String slug,

        @NotBlank(message = "Nội dung trang không được để trống")
        @Size(max = 50000, message = "Nội dung trang tối đa 50000 ký tự")
        String content,

        @Size(max = 150, message = "SEO title tối đa 150 ký tự")
        String metaTitle,

        @Size(max = 300, message = "SEO description tối đa 300 ký tự")
        String metaDescription
) {
}