package com.tuanviet.gaminggear.service.impl;

import com.tuanviet.gaminggear.dto.request.AddCartItemRequest;
import com.tuanviet.gaminggear.dto.request.UpdateCartItemRequest;
import com.tuanviet.gaminggear.dto.response.CartItemResponse;
import com.tuanviet.gaminggear.dto.response.CartResponse;
import com.tuanviet.gaminggear.entity.auth.User;
import com.tuanviet.gaminggear.entity.cart.Cart;
import com.tuanviet.gaminggear.entity.cart.CartItem;
import com.tuanviet.gaminggear.entity.catalog.Product;
import com.tuanviet.gaminggear.entity.catalog.ProductVariant;
import com.tuanviet.gaminggear.exception.BadRequestException;
import com.tuanviet.gaminggear.exception.ResourceNotFoundException;
import com.tuanviet.gaminggear.mapper.CartMapper;
import com.tuanviet.gaminggear.repository.CartItemRepository;
import com.tuanviet.gaminggear.repository.CartRepository;
import com.tuanviet.gaminggear.repository.ProductVariantRepository;
import com.tuanviet.gaminggear.repository.UserRepository;
import com.tuanviet.gaminggear.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .map(this::toCartResponse)
                .orElseGet(this::emptyCartResponse);
    }


    @Override
    @Transactional
    public CartResponse addItem(Long userId, AddCartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        ProductVariant variant = getAvailableVariant(request.variantId());

        CartItem cartItem = itemRepository
                .findByCartIdAndProductVariantId(cart.getId(),variant.getId())
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                            newCartItem.setCart(cart);
                            newCartItem.setProductVariant(variant);
                            newCartItem.setQuantity(0);
                            return newCartItem;
                });
        int newQuantity = cartItem.getQuantity() + request.quantity();
        validateStock(variant,newQuantity);

        cartItem.setQuantity(newQuantity);
        itemRepository.save(cartItem);

        return getCart(userId);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        CartItem cartItem = itemRepository.findByIdAndCartUserId(cartItemId,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));
        ProductVariant variant = cartItem.getProductVariant();
        getAvailableVariant(variant.getId());
        validateStock(variant,request.quantity());
        cartItem.setQuantity(request.quantity());
        itemRepository.save(cartItem);
        return getCart(userId);
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long userId, Long cartItemId) {
        CartItem cartItem = itemRepository.findByIdAndCartUserId(cartItemId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);

        return toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart == null) {
            return emptyCartResponse();
        }

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    private Cart getOrCreateCart(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
        });
    }

    private CartResponse toCartResponse(Cart cart){
        List<CartItemResponse> items = cart.getCartItems()
                .stream()
                .map(cartMapper::toCartItemResponse)
                .toList();
        int totalQuantity = items.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();
        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(
                cart.getId(),
                items,
                totalQuantity,
                totalPrice
        );
    }

    private CartResponse emptyCartResponse() {
        return new CartResponse(
                null,
                List.of(),
                0,
                BigDecimal.ZERO
        );
    }

    private ProductVariant getAvailableVariant(Long variantId){
          ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy variant sản phẩm"));
          validateAvailableVariant(variant);
          return variant;
    }

    private void validateAvailableVariant (ProductVariant variant){
        Product product = variant.getProduct();

        if(!variant.isActive()
                || !product.getBrand().isActive()
                || !product.getCategory().isActive()
                || !product.isActive()){
            throw new BadRequestException("Sản phẩm đang không khả dụng");
        }
    }

    private void validateStock (ProductVariant variant, int quantity){
        if(variant.getStockQuantity() == 0){
            throw new BadRequestException("Sản phẩm đã hết hàng");
        }
        if(quantity > variant.getStockQuantity()){
            throw new BadRequestException("Sản phẩm không còn đủ hàng cho số lượng này");
        }
    }
}
