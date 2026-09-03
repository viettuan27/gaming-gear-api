package com.tuanviet.gaminggear.mapper;

import com.tuanviet.gaminggear.dto.response.CartItemResponse;
import com.tuanviet.gaminggear.entity.cart.CartItem;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {

    public CartItemResponse toCartItemResponse(CartItem cartItem){
        ProductVariant productVariant = cartItem.getProductVariant();
        Product product = productVariant.getProduct();

        BigDecimal subtotal = productVariant.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemResponse(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                productVariant.getId(),
                productVariant.getName(),
                productVariant.getSku(),
                productVariant.getPrice(),
                cartItem.getQuantity(),
                subtotal
        );
    }

}
