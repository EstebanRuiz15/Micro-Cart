package com.emazon.micro_cart.infraestructur.driven_rp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;

public interface IRepositoryCartItemsJpa extends JpaRepository<CartItemsEntity,Integer>{
    
}
