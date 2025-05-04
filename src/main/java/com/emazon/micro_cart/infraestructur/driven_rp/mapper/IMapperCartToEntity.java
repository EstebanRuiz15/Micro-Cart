package com.emazon.micro_cart.infraestructur.driven_rp.mapper;

import org.mapstruct.Mapper;

import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IMapperCartToEntity {

    CartEntity toCartEntity(Cart cart);
    Cart toCart(CartEntity cartEntity);
    CartItemsEntity toCartItemsEntity(CartItems cartItems);
    CartItems toCartItems(CartItemsEntity cartItemsEntity);
    List<CartItems> toListCartItems(List<CartItemsEntity> list);
}
