package com.emazon.micro_cart.domain.services;

import org.springframework.stereotype.Service;

import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.domain.interfaces.IRepositoryCart;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import java.util.Optional;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartServicePort {
    private final IStockServicePort stockService;
    private final IRepositoryCart repository;

    @Override
    public void addItemsToCart(Integer id, Integer quantity) {
        if (!stockService.validItemExist(id)) {
            throw new ErrorNotFoudArticle("not found article: " + id);
        }
        Integer itemQuantity = stockService.validItemQuantity(id, quantity);

        if (itemQuantity == 0) {
            throw new ErrorExceptionQuantity("in 7 days arrive supply to this ");
        }

        if (itemQuantity < quantity) {
            throw new ErrorExceptionQuantity("in this moment only have " + quantity + " products");
        }
        int userid = 0;

        Optional<Cart> cart = repository.findByUserId(userid);

        if (!cart.isPresent()) {
            Cart car = createNewCart(id);
            addProductToCart(car, id, quantity);
        } else {
            addProductToCart(cart.get(), id, quantity);
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
        cartItem.setCartEntity(cart);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        repository.addItemToCart(cart);
    }
}
