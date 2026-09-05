package com.tuanviet.gaminggear.dto.request;

import com.tuanviet.gaminggear.entity.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Trạng thái đơn hàng không được để trống")
        OrderStatus status
) {
}
