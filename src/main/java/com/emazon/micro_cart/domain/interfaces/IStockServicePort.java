package com.emazon.micro_cart.domain.interfaces;
import java.util.List;

public interface IStockServicePort {
    boolean validItemExist(Integer id);
    Integer validItemQuantity(Integer id);
    boolean validCategories(List<Integer> idarticles);
}