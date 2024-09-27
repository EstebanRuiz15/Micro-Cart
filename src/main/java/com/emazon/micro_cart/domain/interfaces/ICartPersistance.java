package com.emazon.micro_cart.domain.interfaces;

import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;

import java.util.Optional;

public interface ICartPersistance {
    void addItemToCart(Cart cart, CartItems cartItems);
    Optional<Cart> findByUserId(Integer id);
    Integer getClientId();
    void save(Cart cart);
}
