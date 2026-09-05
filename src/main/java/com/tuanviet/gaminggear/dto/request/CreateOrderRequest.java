package com.tuanviet.gaminggear.dto.request;

import com.tuanviet.gaminggear.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @NotBlank(message = "Tên người nhận không được để trống")
        @Size(max = 100,message = "Tên người nhận tối đa 100 ký tự")
        String recipientName,

        @NotBlank(message = "Số điện thoại người nhận không được để trống")
        @PhoneNumber
        String recipientPhone,

        @NotBlank(message = "Địa chỉ giao hàng không được để trống")
        @Size(max = 500, message = "Địa chỉ giao hàng tối đa 500 ký tự")
        String shippingAddress,

        @Size(max = 500,message = "Ghi chú tối đa 500 ký tự")
        String note
) {
}
