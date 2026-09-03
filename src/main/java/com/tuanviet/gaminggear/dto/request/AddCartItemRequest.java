package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
        @NotNull(message = "Variant sản phẩm không được để trống")
        Long variantId,

        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1,message = "Số lượng phải lớn hơn 0")
        Integer quantity
) {
}
