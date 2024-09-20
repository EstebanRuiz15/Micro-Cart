package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Component;

import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.infraestructur.driving_http.feign.StockClient;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceFeignImple implements IStockServicePort {

    private final StockClient stockClient;

    @Override
    public boolean validItemExist(Integer id) {
        return stockClient.validItemExist(id);
    };

    public Integer validItemQuantity(Integer id, Integer quantity){
        return stockClient.validQuantityItems(id,quantity);
    };
}
