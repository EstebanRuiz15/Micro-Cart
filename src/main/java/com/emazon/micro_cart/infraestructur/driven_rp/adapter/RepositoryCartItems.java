package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Service;

import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.infraestructur.driven_rp.persistence.IRepositoryCartItemsJpa;
import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RepositoryCartItems implements IRepositoryItemsPort {
    private final IRepositoryCartItemsJpa repositoryJpa;
    @Override
    public List<Integer> getAllArticlesId(Integer idCart){
        return repositoryJpa.findItemIdsByCartId(idCart);
    };
}
