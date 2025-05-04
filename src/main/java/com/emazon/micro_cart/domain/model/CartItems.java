package com.emazon.micro_cart.domain.model;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;

public class CartItems {
     private Integer id;
    private CartEntity cart;
    private Integer productId; 
    private Integer quantity;
    
    public CartItems() {
    }

    public CartItems(Integer id, CartEntity cart, Integer productId, Integer quantity) {
        this.id = id;
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    
}
