package com.emazon.micro_cart.domain.model;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    private Integer id;

    private Integer userId;

    private LocalDateTime creationDate;

    private LocalDateTime modiDate;

    private Integer quantity;

    private List<CartItemsEntity> items = new ArrayList<>();

    
}
