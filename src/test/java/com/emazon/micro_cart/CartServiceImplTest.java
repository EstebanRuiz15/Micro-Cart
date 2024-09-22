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
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    private Cart cart;
    private CartItems cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new Cart();
        cart.setId(1);
        cart.setUserId(123);

        cartItem = new CartItems();
        cartItem.setProductId(1);
        cartItem.setQuantity(5);
        cartItem.setCart(mapper.toCartEntity(cart));
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

    @Test
    public void testDeleteItemsToCart_ItemDeleted() {
        when(repository.getClientId()).thenReturn(123);
        when(repositoryItems.findByProductIdAndUserId(1, 123)).thenReturn(Optional.of(cartItem));
        when(repository.findByUserId(123)).thenReturn(Optional.of(cart));

        cartService.deleteItemsToCart(1, 5);

        verify(repositoryItems, times(1)).delete(cartItem);
        verify(repository, times(1)).save(cart);
        assertEquals(cart.getModiDate().toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    @Test
    public void testDeleteItemsToCart_ItemQuantityReduced() {
        when(repository.getClientId()).thenReturn(123);
        when(repositoryItems.findByProductIdAndUserId(1, 123)).thenReturn(Optional.of(cartItem));
        when(repository.findByUserId(123)).thenReturn(Optional.of(cart));

        cartService.deleteItemsToCart(1, 3);

        assertEquals(2, cartItem.getQuantity());
        verify(repositoryItems, times(1)).save(cartItem);
        verify(repositoryItems, never()).delete(cartItem);
        verify(repository, times(1)).save(cart);
    }

    @Test
    public void testDeleteItemsToCart_CartItemNotFound() {
        when(repository.getClientId()).thenReturn(123);
        when(repositoryItems.findByProductIdAndUserId(1, 123)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> cartService.deleteItemsToCart(1, 3));

        verify(repositoryItems, never()).delete(any());
        verify(repositoryItems, never()).save(any());
        verify(repository, never()).save(any());
    }

    @Test
    public void testCartAndCartItemPresent_CartItemUpdated() {
        
        Integer userId = 123;
        Integer productId = 1;
        Integer quantity = 3;
        List<Integer> articleIds = Arrays.asList(1, 2, 3);
        Integer itemQuantity = 8; 
    
      
        when(repository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(repositoryItems.getAllArticlesId(cart.getId())).thenReturn(articleIds);
        when(stockService.validCategories(articleIds)).thenReturn(true);
        when(stockService.validItemExist(productId)).thenReturn(true); 
        when(stockService.validItemQuantity(productId)).thenReturn(itemQuantity); 
        when(repositoryItems.findByProductIdAndUserId(productId, userId)).thenReturn(Optional.of(cartItem));
    
        cartService.addItemsToCart(productId, quantity);
        assertEquals(5, cartItem.getQuantity());
    }
}