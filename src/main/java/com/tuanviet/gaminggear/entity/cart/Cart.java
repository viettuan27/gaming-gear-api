package com.tuanviet.gaminggear.entity.cart;

import com.tuanviet.gaminggear.entity.AuditableEntity;
import com.tuanviet.gaminggear.entity.auth.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "carts")
@NoArgsConstructor
public class Cart extends AuditableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
}
