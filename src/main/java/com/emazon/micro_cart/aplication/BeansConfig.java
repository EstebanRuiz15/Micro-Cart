package com.emazon.micro_cart.aplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emazon.micro_cart.domain.interfaces.IRepositoryCart;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.services.CartServiceImpl;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;


@Configuration
public class BeansConfig {

    @Bean
    public CartServiceImpl getCartServiceImpl( IStockServicePort stockService,IRepositoryCart repository,
                                                IRepositoryItemsPort repositoryItems,IMapperCartToEntity mapper){

        return new CartServiceImpl(stockService, repository, repositoryItems, mapper);
    }
   
   
}
