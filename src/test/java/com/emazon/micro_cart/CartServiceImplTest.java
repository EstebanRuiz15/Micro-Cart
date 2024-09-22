package com.emazon.micro_cart;
import com.emazon.micro_cart.domain.exception.*;
import com.emazon.micro_cart.domain.interfaces.*;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.services.CartServiceImpl;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private IStockServicePort stockService;

    @Mock
    private IRepositoryCart repository;

    @Mock
    private IRepositoryItemsPort repositoryItems;

    @Mock
    private IMapperCartToEntity mapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowErrorNotFoundArticleWhenItemDoesNotExist() {
        when(stockService.validItemExist(anyInt())).thenReturn(false);

        ErrorNotFoudArticle exception = assertThrows(ErrorNotFoudArticle.class, () -> {
            cartService.addItemsToCart(1, 5);
        });

        assertEquals(ConstantsDomain.NOT_FOUND_ARTICLE + 1, exception.getMessage());
    }

    @Test
    void shouldThrowErrorExceptionQuantityWhenItemQuantityIsZero() {
        when(stockService.validItemExist(anyInt())).thenReturn(true);
        when(stockService.validItemQuantity(anyInt())).thenReturn(0);

        ErrorExceptionQuantity exception = assertThrows(ErrorExceptionQuantity.class, () -> {
            cartService.addItemsToCart(1, 5);
        });

        assertEquals(ConstantsDomain.MESSAGE_DATE_ARRIVE_SUPPLY, exception.getMessage());
    }

    @Test
    void shouldThrowErrorExceptionQuantityWhenItemQuantityIsLessThanRequested() {
        when(stockService.validItemExist(anyInt())).thenReturn(true);
        when(stockService.validItemQuantity(anyInt())).thenReturn(3);

        ErrorExceptionQuantity exception = assertThrows(ErrorExceptionQuantity.class, () -> {
            cartService.addItemsToCart(1, 5);
        });

        assertEquals(ConstantsDomain.MESSAGE_ONLY_HAVE + 3 + ConstantsDomain.PRODUCTS, exception.getMessage());
    }

    @Test
    void shouldCreateNewCartIfUserHasNoCart() {
        when(stockService.validItemExist(anyInt())).thenReturn(true);
        when(stockService.validItemQuantity(anyInt())).thenReturn(10);
        when(repository.getClientId()).thenReturn(1);
        when(repository.findByUserId(anyInt())).thenReturn(Optional.empty());

        cartService.addItemsToCart(1, 5);

        verify(repository, times(1)).addItemToCart(any(Cart.class), any(CartItems.class));
    }

    @Test
    void shouldAddItemToExistingCart() {
        Cart existingCart = new Cart();
        when(stockService.validItemExist(anyInt())).thenReturn(true);
        when(stockService.validItemQuantity(anyInt())).thenReturn(10);
        when(repository.getClientId()).thenReturn(1);
        when(repository.findByUserId(anyInt())).thenReturn(Optional.of(existingCart));
        when(repositoryItems.getAllArticlesId(anyInt())).thenReturn(List.of(1, 2));

        when(stockService.validCategories(anyList())).thenReturn(true);

        cartService.addItemsToCart(1, 5);

        verify(repository, times(1)).addItemToCart(any(Cart.class), any(CartItems.class));
    }

    @Test
    void shouldThrowErrorExceptionCategoriesRepitWhenTooManyCategoriesRepeat() {
        Cart existingCart = new Cart();
        when(stockService.validItemExist(anyInt())).thenReturn(true);
        when(stockService.validItemQuantity(anyInt())).thenReturn(10);
        when(repository.getClientId()).thenReturn(1);
        when(repository.findByUserId(anyInt())).thenReturn(Optional.of(existingCart));
        when(repositoryItems.getAllArticlesId(anyInt())).thenReturn(List.of(1, 2));
        when(stockService.validCategories(anyList())).thenReturn(false);

        ErrorExceptionCategoriesRepit exception = assertThrows(ErrorExceptionCategoriesRepit.class, () -> {
            cartService.addItemsToCart(1, 5);
        });

        assertEquals(ConstantsDomain.ERROR_3_CATEGORIES_REPEAT, exception.getMessage());
    }

    @Test
    void shouldThrowErrorFeignExceptionWhenFeignClientFails() {
        when(stockService.validItemExist(anyInt())).thenThrow(FeignException.class);

        ErrorFeignException exception = assertThrows(ErrorFeignException.class, () -> {
            cartService.addItemsToCart(1, 5);
        });

        assertTrue(exception.getMessage().contains(ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
    }

    @Test
    void shouldAddItemsToCartSuccessfully() {
        
        when(stockService.validItemExist(1)).thenReturn(true);
        when(stockService.validItemQuantity(1)).thenReturn(10);
        when(repository.getClientId()).thenReturn(1);

        Cart existingCart = new Cart();
        when(repository.findByUserId(1)).thenReturn(Optional.of(existingCart));

        when(repositoryItems.getAllArticlesId(existingCart.getId())).thenReturn(List.of(1, 2));
        when(stockService.validCategories(List.of(1, 2))).thenReturn(true);

        when(mapper.toCartEntity(any(Cart.class))).thenReturn(new CartEntity());

        cartService.addItemsToCart(1, 5);

        verify(repository, times(1)).addItemToCart(any(Cart.class), any(CartItems.class));
    }
}