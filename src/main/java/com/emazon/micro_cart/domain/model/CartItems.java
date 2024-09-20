package com.emazon.micro_cart.domain.model;

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
    private Cart cartEntity;
    private Integer productId; 
    private Integer quantity;
}
