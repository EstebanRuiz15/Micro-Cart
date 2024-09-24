package com.emazon.micro_cart.domain.services;

import com.emazon.micro_cart.domain.exception.ErrorExceptionCategoriesRepit;
import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.exception.ErrorNotFoundArticleToDelete;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.domain.interfaces.IRepositoryCart;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public class CartServiceImpl implements ICartServicePort {
    private final IStockServicePort stockService;
    private final IRepositoryCart repository;
    private final IRepositoryItemsPort repositoryItems;

    public CartServiceImpl(IStockServicePort stockService, IRepositoryCart repository,
            IRepositoryItemsPort repositoryItems) {
        this.stockService = stockService;
        this.repository = repository;
        this.repositoryItems = repositoryItems;
    }

    @Override
    public String addItemsToCart(Integer id, Integer quantity) {
        Integer userid = repository.getClientId();
        Cart cart;

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

        if (!repository.findByUserId(userid).isPresent()) {
            cart = createNewCart(userid);
            repository.save(cart);
            addProductToCart(cart, id, quantity);
            return ConstantsDomain.ADD_WITH_EXIT;
        }

        if (repository.findByUserId(userid).isPresent()) {
            cart = repository.findByUserId(userid).get();
            addItemIfCarExist(cart, id, quantity, userid);

            return ConstantsDomain.ADD_WITH_EXIT;
        }

        return ConstantsDomain.ADD_WITH_EXIT;
    };

    @Override
    public String deleteItemsToCart(Integer idArticle, Integer quantity) {
        Integer clientId = repository.getClientId();
        Optional <CartItems> items = repositoryItems.findByProductIdAndUserId(idArticle, clientId);
        if(!items.isPresent()){
            throw new ErrorNotFoundArticleToDelete(ConstantsDomain.NOT_ARTICLE_TO_DELETE);
        }
        CartItems item=items.get();
        if ((item.getQuantity() - quantity) <= 0) {
            repositoryItems.delete(item);
        }
        if ((item.getQuantity() - quantity) > 0) {
            item.setQuantity(item.getQuantity() - quantity);
            repositoryItems.save(item);
        }
        Cart cart = repository.findByUserId(clientId).get();
        cart.setModiDate(LocalDateTime.now());
        repository.save(cart);
        return ConstantsDomain.DELETE_WHIT_SUCESS;
    }

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
        repository.addItemToCart(cart, cartItem);
    }

    private void addItemIfCarExist(Cart cart, Integer id, Integer quantity, Integer userid) {
        
        List<Integer> ids = repositoryItems.getAllArticlesId(cart.getId());
        if (stockService.validCategories(ids)) {
            Optional<CartItems> cartItem = repositoryItems.findByProductIdAndUserId(id, userid);
            if (cartItem.isPresent()) {
                CartItems item = cartItem.get();
                cart.setModiDate(LocalDateTime.now());
                item.setQuantity(item.getQuantity() + quantity);
                repository.save(cart);
                repositoryItems.save(item);
            } 
            if(!cartItem.isPresent()) {
                cart.setModiDate(LocalDateTime.now());
                addProductToCart(cart, id, quantity);
            }
        } else {
            throw new ErrorExceptionCategoriesRepit(ConstantsDomain.ERROR_3_CATEGORIES_REPEAT);
        }
    }
}
