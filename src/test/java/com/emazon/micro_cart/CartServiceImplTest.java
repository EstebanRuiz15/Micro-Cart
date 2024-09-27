package com.emazon.micro_cart;

import com.emazon.micro_cart.domain.exception.*;
import com.emazon.micro_cart.domain.interfaces.*;
import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.model.CategoryMod;
import com.emazon.micro_cart.domain.model.PaginItems;
import com.emazon.micro_cart.domain.services.CartServiceImpl;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private IStockServicePort stockService;

    @Mock
    private ICartPersistance repository;

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
        when(stockService.validItemExist(anyInt()))
                .thenThrow(new ErrorFeignException(ConstantsDomain.COMUNICATION_ERROR_WITH_SERVICE));
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

        assertThrows(ErrorNotFoundArticleToDelete.class, () -> cartService.deleteItemsToCart(1, 3));

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

    @Test
    public void getAllItemsToCart_ShouldReturnPaginatedItems_WhenCartExists() {
        // Arrange
        Integer userId = 1;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        String orderBy = "name";
        String filterBy = null;
        String nameFilter = null;

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(userId);
        cart.setCreationDate(LocalDateTime.now());

        CartItems item1 = new CartItems();
        item1.setProductId(1);
        item1.setQuantity(2);

        CartItems item2 = new CartItems();
        item2.setProductId(2);
        item2.setQuantity(1);

        when(repository.getClientId()).thenReturn(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(repositoryItems.getAllArticlesId(cart.getId())).thenReturn(Arrays.asList(1, 2));
        when(stockService.allArticles(Arrays.asList(1, 2))).thenReturn(Arrays.asList(
            new ArticlesMod(1, "Article 1", "Description 1", 10.0, 20, null, 2, "Brand1", null),
            new ArticlesMod(2, "Article 2", "Description 2", 15.0, 15, null, 1, "Brand2", null)
        ));
        
        // Act
        PaginItems result = cartService.getAllIemsToCart(pageSize, pageNumber, orderBy, filterBy, nameFilter);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
    }

    @Test
    public void getAllItemsToCart_ShouldThrowErrorExceptionConflict_WhenCartDoesNotExist() {
        
        Integer userId = 1;
        when(repository.getClientId()).thenReturn(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

    }

    @Test
    public void getAllItemsToCart_ShouldThrowErrorExceptionConflict_WhenNoArticlesInCart() {
        
        Integer userId = 1;
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(userId);
        cart.setCreationDate(LocalDateTime.now());

        when(repository.getClientId()).thenReturn(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(repositoryItems.getAllArticlesId(cart.getId())).thenReturn(null);

       
        ErrorExceptionConflict thrown = assertThrows(ErrorExceptionConflict.class, () -> {
            cartService.getAllIemsToCart(10, 0, "name", null, null);
        });
        assertThat(thrown.getMessage()).isEqualTo(ConstantsDomain.NO_ARTICLES_IN_CART);
    }

    @Test
    public void getAllItemsToCart_ShouldReturnFilteredItemsByCategory_WhenCategoryFilterIsProvided() {
        // Arrange
        Integer userId = 1;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        String orderBy = "name";
        String filterBy = ConstantsDomain.CATEGORIES;
        String nameFilter = "Category1";

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(userId);
        cart.setCreationDate(LocalDateTime.now());

        ArticlesMod article1 = new ArticlesMod(1, "Article 1", "Description 1", 10.0, 20, null, 2, "Brand1", 
            Arrays.asList(new CategoryMod("Category1","des")));
        ArticlesMod article2 = new ArticlesMod(2, "Article 2", "Description 2", 15.0, 15, null, 1, "Brand2", 
            Arrays.asList(new CategoryMod("Category2","des")));

        when(repository.getClientId()).thenReturn(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(repositoryItems.getAllArticlesId(cart.getId())).thenReturn(Arrays.asList(1, 2));
        when(stockService.allArticles(Arrays.asList(1, 2))).thenReturn(Arrays.asList(article1, article2));

        PaginItems result = cartService.getAllIemsToCart(pageSize, pageNumber, orderBy, filterBy, nameFilter);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1); 
        assertThat(result.getItems().get(0).getName()).isEqualTo("Article 1");
    }

    @Test
    public void getAllItemsToCart_ShouldReturnFilteredItemsByBrand_WhenBrandFilterIsProvided() {
        
        Integer userId = 1;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        String orderBy = "name";
        String filterBy = ConstantsDomain.BRAND;
        String nameFilter = "Brand1";

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(userId);
        cart.setCreationDate(LocalDateTime.now());

        ArticlesMod article1 = new ArticlesMod(1, "Article 1", "Description 1", 10.0, 20, null, 2, "Brand1", null);
        ArticlesMod article2 = new ArticlesMod(2, "Article 2", "Description 2", 15.0, 15, null, 1, "Brand2", null);
        
        when(repository.getClientId()).thenReturn(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(repositoryItems.getAllArticlesId(cart.getId())).thenReturn(Arrays.asList(1, 2));
        when(stockService.allArticles(Arrays.asList(1, 2))).thenReturn(Arrays.asList(article1, article2));

        PaginItems result = cartService.getAllIemsToCart(pageSize, pageNumber, orderBy, filterBy, nameFilter);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1); 
        assertThat(result.getItems().get(0).getName()).isEqualTo("Article 1");
    }
}