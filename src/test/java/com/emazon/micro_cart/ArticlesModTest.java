package com.emazon.micro_cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.model.CategoryMod;

public class ArticlesModTest {
     @Test
    public void testConstructorAndGetters() {
        List<CategoryMod> categories = Arrays.asList(new CategoryMod("name", "Category1"), new CategoryMod("name", "Category2"));

        ArticlesMod article = new ArticlesMod(1, "Test Article", "This is a test article.", 100.50, 10, null, 2, "Test Brand", categories);

        assertEquals(1, article.getId());
        assertEquals("Test Article", article.getName());
        assertEquals("This is a test article.", article.getDescription());
        assertEquals(100.50, article.getPrice());
        assertEquals(10, article.getQuantityStock());
        assertNull(article.getStockMessage()); 
        assertEquals(2, article.getQuantityInCart());
        assertEquals("Test Brand", article.getBrandName());
        assertEquals(categories, article.getCategories());
    }

    @Test
    public void testSetters() {
        ArticlesMod article = new ArticlesMod();

        article.setId(2);
        article.setName("Updated Article");
        article.setDescription("Updated description.");
        article.setPrice(200.75);
        article.setQuantityStock(15);
        article.setStockMessage("Low stock");
        article.setQuantityInCart(3);
        article.setBrandName("Updated Brand");

        List<CategoryMod> newCategories = Arrays.asList(new CategoryMod("name", "Category3"));
        article.setCategories(newCategories);

        assertEquals(2, article.getId());
        assertEquals("Updated Article", article.getName());
        assertEquals("Updated description.", article.getDescription());
        assertEquals(200.75, article.getPrice());
        assertEquals(15, article.getQuantityStock());
        assertEquals("Low stock", article.getStockMessage());
        assertEquals(3, article.getQuantityInCart());
        assertEquals("Updated Brand", article.getBrandName());
        assertEquals(newCategories, article.getCategories());
    }

    @Test
    public void testNullCategories() {
        ArticlesMod article = new ArticlesMod(3, "Null Categories Article", "Test description.", 50.00, 5, "In stock", 1, "Brand X", null);

        assertNull(article.getCategories());
    }

    @Test
    public void testEmptyConstructor() {
    
        ArticlesMod article = new ArticlesMod();

        assertNull(article.getId());
        assertNull(article.getName());
        assertNull(article.getDescription());
        assertNull(article.getPrice());
        assertNull(article.getQuantityStock());
        assertNull(article.getStockMessage());
        assertNull(article.getQuantityInCart());
        assertNull(article.getBrandName());
        assertNull(article.getCategories());
    }

    @Test
    public void testJsonIncludeStockMessage() {
        ArticlesMod article = new ArticlesMod();
        article.setStockMessage(null);

        assertNull(article.getStockMessage());
    }
}
