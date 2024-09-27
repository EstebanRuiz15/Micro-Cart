package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Component;

import com.emazon.micro_cart.domain.exception.ErrorFeignException;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import com.emazon.micro_cart.infraestructur.feign.StockClient;

import feign.FeignException;

import java.util.List;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceFeignImple implements IStockServicePort {

    private final StockClient stockClient;

    @Override
    public boolean validItemExist(Integer id) {
        try {
            return stockClient.validItemExist(id);
        } catch (FeignException e) {
            throw new ErrorFeignException((ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
        }
    };

    @Override
    public Integer validItemQuantity(Integer id) {
        try {
            return stockClient.validQuantityItems(id);
        } catch (FeignException e) {
            throw new ErrorFeignException((ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
        }
    };

    @Override
    public boolean validCategories(List<Integer> idarticles) {
        try {
            return stockClient.validCategories(idarticles);
        } catch (FeignException e) {
            throw new ErrorFeignException((ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
        }
    };

    @Override
    public List<ArticlesMod> allArticles(List<Integer> idsArticles){
        try {
            return stockClient.getAllArticlestoId(idsArticles);
        } catch (FeignException e) {
              throw new ErrorFeignException((ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
        }
    }
}
