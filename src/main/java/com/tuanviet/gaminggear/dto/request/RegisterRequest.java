package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email không được để trống")
        @Size(max = 50, message = "Email tối đa 50 ký tự")
        @Email(message = "Email không hợp lệ")
        String email,

        @NotBlank(message = "Tên không được để trống")
        String fullName,

        @Size(max = 11,message = "Số điện thoại không hợp lệ")
        String phone,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min=7,max = 50, message = "Mật khẩu phải từ 7 đến 50 ký tự")
        String password
) {
}
