package com.emazon.micro_cart.domain.interfaces;

public interface ICartServicePort {
    void addItemsToCart(Integer id, Integer quantity);
    void deleteItemsToCart(Integer id, Integer quantity);
}
