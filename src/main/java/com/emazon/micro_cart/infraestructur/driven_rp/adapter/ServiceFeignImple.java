package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Component;

import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.infraestructur.feign.StockClient;

import java.util.List;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceFeignImple implements IStockServicePort {

    private final StockClient stockClient;

    @Override
    public boolean validItemExist(Integer id) {
        return stockClient.validItemExist(id);
    };

    @Override
    public Integer validItemQuantity(Integer id){
        return stockClient.validQuantityItems(id);
    };

    @Override
    public boolean validCategories(List<Integer> idarticles){
        return stockClient.validCategories(idarticles);
    };
}
