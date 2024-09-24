package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Service;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.persistence.IRepositoryCartItemsJpa;

import java.util.List;
import lombok.AllArgsConstructor;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RepositoryCartItems implements IRepositoryItemsPort {
    private final IRepositoryCartItemsJpa repositoryJpa;
    private final IMapperCartToEntity mapper;

    @Override
    public List<Integer> getAllArticlesId(Integer idCart) {
        return repositoryJpa.findItemIdsByCartId(idCart);
    }

    @Override
    public Integer getQuantityItemsToId(Integer idArticle, Integer idClient) {
        return repositoryJpa.findTotalQuantityByClientAndArticle(idClient, idArticle);
    }

    @Override
    public void delete(CartItems cartItems) {
         repositoryJpa.delete(mapper.toCartItemsEntity(cartItems));
        
    }

    @Override
    public Optional<CartItems> findByProductIdAndUserId(Integer productId, Integer userId) {
         return repositoryJpa.findByProductIdAndUserId(productId, userId)
                 .map(mapper::toCartItems);
        
    }

    @Override
    public void save(CartItems cartItems) {
        repositoryJpa.save(mapper.toCartItemsEntity(cartItems));
        
    }

    /*
     * @Override
     * public PaginItems getPaginatedCarItems(List<ArticlesMod> articles,Integer
     * cartId,Integer size,Integer page){
     * Pageable pageable= PageRequest.of(page, size);
     * Page<CartItemsEntity> cartItemsPage =
     * repositoryJpa.findItemIdsByCartId(cartId, pageable);
     * 
     * paginItems.setItems(articles);
     * paginItems.setQuantity(pageable.);
     * 
     * }
     */
}
