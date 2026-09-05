package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.OrderItemResponse;
import com.tuanviet.gaminggear.dto.response.OrderResponse;
import com.tuanviet.gaminggear.dto.response.OrderSummaryResponse;
import com.tuanviet.gaminggear.entity.order.Order;
import com.tuanviet.gaminggear.entity.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "productVariantId", source = "variant.id")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "items", source = "orderItems")
    OrderResponse toResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    OrderSummaryResponse toSummaryResponse(Order order);
}
