package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    Optional<CartItem> findByIdAndCartUserId(Long cartItemId, Long userId);

    @Query("""
        select cartItem.productVariant.id
        from CartItem cartItem
        where cartItem.cart.user.id = :userId
        """)
    List<Long> findProductVariantIdsByCartUserId(@Param("userId") Long userId);
}