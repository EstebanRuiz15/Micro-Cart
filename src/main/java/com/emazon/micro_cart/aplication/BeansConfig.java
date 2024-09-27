package com.emazon.micro_cart.aplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emazon.micro_cart.domain.interfaces.ICartPersistance;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.services.CartServiceImpl;


@Configuration
public class BeansConfig {

    @Bean
    public CartServiceImpl getCartServiceImpl( IStockServicePort stockService,ICartPersistance repository,
                                                IRepositoryItemsPort repositoryItems){

        return new CartServiceImpl(stockService, repository, repositoryItems);
    }
   
}
