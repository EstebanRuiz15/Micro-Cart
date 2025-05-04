package com.emazon.micro_cart.infraestructur.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.emazon.micro_cart.domain.model.ArticlesMod;

@FeignClient(name = "apistock", url = "http://localhost:7070", configuration = FeignConfig.class)
public interface StockClient {
    @GetMapping("article/exist")
    boolean validItemExist(@RequestParam("idArticle") Integer idArticle);
    @GetMapping("article/quantityValid")
    Integer validQuantityItems(@RequestParam("idArticle") Integer idArticle);
    @GetMapping("article/categoriesValid")
    boolean validCategories(@RequestParam List<Integer> listId);
    @GetMapping("article/getArticlesToId")
    List<ArticlesMod> getAllArticlestoId(@RequestParam List<Integer> ids);

}