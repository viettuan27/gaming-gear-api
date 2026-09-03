package com.tuanviet.gaminggear.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1,message = "Số lượng phải lớn hơn 0")
        Integer quantity
) {
}
