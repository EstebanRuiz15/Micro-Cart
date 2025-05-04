package com.emazon.micro_cart.domain.interfaces;

import java.util.List;

import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.model.PaginItems;

import java.util.Optional;

public interface IRepositoryItemsPort {
    List<Integer> getAllArticlesId(Integer idCart);
    Integer getQuantityItemsToId(Integer idItem,Integer IdClient);
    void delete(CartItems cartItems);
    Optional<CartItems> findByProductIdAndUserId( Integer productId, Integer userId);
    void save(CartItems cartItems);
    List<CartItems> getAllItems(Integer id);
    PaginItems getPaginatedCarItems(List<ArticlesMod> items,Integer size,Integer page); 
    void deleteByCartId(Integer idCart);
    
}
