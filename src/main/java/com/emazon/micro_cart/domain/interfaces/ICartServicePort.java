package com.emazon.micro_cart.domain.interfaces;

import com.emazon.micro_cart.domain.model.PaginItems;

public interface ICartServicePort {
    String addItemsToCart(Integer id, Integer quantity);
    String deleteItemsToCart(Integer id, Integer quantity);
    PaginItems getAllIemsToCart(Integer size,Integer page,String OrderBy,String filterBy, String nameFilter);
    
}
