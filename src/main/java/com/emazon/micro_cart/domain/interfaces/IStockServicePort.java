package com.emazon.micro_cart.domain.interfaces;
import java.util.List;

import com.emazon.micro_cart.domain.model.ArticlesMod;

public interface IStockServicePort {
    boolean validItemExist(Integer id);
    Integer validItemQuantity(Integer id);
    boolean validCategories(List<Integer> idarticles);
    List<ArticlesMod> allArticles(List<Integer> idsArticles);
}