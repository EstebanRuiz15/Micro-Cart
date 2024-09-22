package com.emazon.micro_cart.domain.interfaces;

import java.util.List;

public interface IRepositoryItemsPort {
    List<Integer> getAllArticlesId(Integer idCart);
}
