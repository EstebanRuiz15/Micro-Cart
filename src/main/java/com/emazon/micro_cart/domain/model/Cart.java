package com.emazon.micro_cart.domain.model;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class Cart {
    private Integer id;

    private Integer userId;

    private LocalDateTime creationDate;

    private LocalDateTime modiDate;

    private List<CartItemsEntity> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(Integer id, Integer userId, LocalDateTime creationDate, LocalDateTime modiDate,
            List<CartItemsEntity> items) {
        this.id = id;
        this.userId = userId;
        this.creationDate = creationDate;
        this.modiDate = modiDate;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModiDate() {
        return modiDate;
    }

    public void setModiDate(LocalDateTime modiDate) {
        this.modiDate = modiDate;
    }

    public List<CartItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<CartItemsEntity> items) {
        this.items = items;
    }

    
}
