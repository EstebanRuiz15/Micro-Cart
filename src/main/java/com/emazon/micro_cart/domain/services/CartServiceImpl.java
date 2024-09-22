package com.emazon.micro_cart.domain.services;

import com.emazon.micro_cart.domain.exception.ErrorExceptionCategoriesRepit;
import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorFeignException;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.domain.interfaces.IRepositoryCart;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;
import feign.FeignException;
import java.util.List;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartServiceImpl implements ICartServicePort {
    private final IStockServicePort stockService;
    private final IRepositoryCart repository;
    private final IRepositoryItemsPort repositoryItems;
    private final IMapperCartToEntity mapper;

    @Override
    public void addItemsToCart(Integer id, Integer quantity) {
        try {
            if (!stockService.validItemExist(id)) {
                throw new ErrorNotFoudArticle(ConstantsDomain.NOT_FOUND_ARTICLE + id);
            }
            Integer itemQuantity = stockService.validItemQuantity(id);

            if (itemQuantity == 0) {
                throw new ErrorExceptionQuantity(ConstantsDomain.MESSAGE_DATE_ARRIVE_SUPPLY);
            }

            if (itemQuantity < quantity) {
                throw new ErrorExceptionQuantity(
                        ConstantsDomain.MESSAGE_ONLY_HAVE + itemQuantity + ConstantsDomain.PRODUCTS);
            }
            Integer userid = repository.getClientId();

            if (!repository.findByUserId(userid).isPresent()) {
                Cart car = createNewCart(userid);
                addProductToCart(car, id, quantity);
            } else {
                Cart cart = repository.findByUserId(userid).get();
                List<Integer> ids = repositoryItems.getAllArticlesId(cart.getId());
                if (stockService.validCategories(ids)) {
                    cart.setModiDate(LocalDateTime.now());
                    addProductToCart(cart, id, quantity);
                } else {
                    throw new ErrorExceptionCategoriesRepit(ConstantsDomain.ERROR_3_CATEGORIES_REPEAT);
                }
            }

        } catch (FeignException e) {
            throw new ErrorFeignException((ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE + e.getMessage() + e));
        }
    };

    private Cart createNewCart(Integer userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setCreationDate(LocalDateTime.now());

        return newCart;
    }

    private void addProductToCart(Cart cart, Integer productId, Integer quantity) {
        CartItems cartItem = new CartItems();
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setCart(mapper.toCartEntity(cart));
        repository.addItemToCart(cart, cartItem);
    }
}
