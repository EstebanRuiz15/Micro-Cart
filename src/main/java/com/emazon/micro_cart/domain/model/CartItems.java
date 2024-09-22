package com.emazon.micro_cart.domain.model;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CartItems {
     private Integer id;
    private CartEntity cart;
    private Integer productId; 
    private Integer quantity;
}
