package com.emazon.micro_cart.domain.interfaces;

public interface ICartServicePort {
    String addItemsToCart(Integer id, Integer quantity);
    String deleteItemsToCart(Integer id, Integer quantity);
    
}
