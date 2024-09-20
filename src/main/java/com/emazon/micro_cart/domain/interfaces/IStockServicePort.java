package com.emazon.micro_cart.domain.interfaces;

public interface IStockServicePort {
    boolean validItemExist(Integer id);
    Integer validItemQuantity(Integer id, Integer quantity);
}