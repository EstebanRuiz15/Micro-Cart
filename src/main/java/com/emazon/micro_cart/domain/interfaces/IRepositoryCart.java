package com.emazon.micro_cart.domain.interfaces;

import com.emazon.micro_cart.domain.model.Cart;
import java.util.Optional;

public interface IRepositoryCart {
    void addItemToCart(Cart cart);
    Optional<Cart> findByUserId(Integer id);
}
