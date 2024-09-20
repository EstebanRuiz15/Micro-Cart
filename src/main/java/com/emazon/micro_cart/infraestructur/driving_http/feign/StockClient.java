package com.emazon.micro_cart.infraestructur.driving_http.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apistock", url = "http://localhost:7070", configuration = FeignConfig.class)
public interface StockClient {
    @PatchMapping("article/increment")
    boolean validItemExist(@RequestParam("idArticle") Integer idArticle);

    Integer validQuantityItems(@RequestParam("idArticle") Integer idArticle,
            @RequestParam("quantity") Integer quantity);

    boolean validCategories(@RequestParam)
}