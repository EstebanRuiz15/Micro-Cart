package com.emazon.micro_cart.infraestructur.driven_rp.mapper;

import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-24T23:46:55-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.1.jar, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class IMapperCartToEntityImpl implements IMapperCartToEntity {

    @Override
    public CartEntity toCartEntity(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartEntity cartEntity = new CartEntity();

        cartEntity.setId( cart.getId() );
        cartEntity.setUserId( cart.getUserId() );
        if ( cart.getCreationDate() != null ) {
            cartEntity.setCreationDate( Date.from( cart.getCreationDate().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( cart.getModiDate() != null ) {
            cartEntity.setModiDate( Date.from( cart.getModiDate().toInstant( ZoneOffset.UTC ) ) );
        }
        List<CartItemsEntity> list = cart.getItems();
        if ( list != null ) {
            cartEntity.setItems( new ArrayList<CartItemsEntity>( list ) );
        }

        return cartEntity;
    }

    @Override
    public Cart toCart(CartEntity cartEntity) {
        if ( cartEntity == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setId( cartEntity.getId() );
        cart.setUserId( cartEntity.getUserId() );
        if ( cartEntity.getCreationDate() != null ) {
            cart.setCreationDate( LocalDateTime.ofInstant( cartEntity.getCreationDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( cartEntity.getModiDate() != null ) {
            cart.setModiDate( LocalDateTime.ofInstant( cartEntity.getModiDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        List<CartItemsEntity> list = cartEntity.getItems();
        if ( list != null ) {
            cart.setItems( new ArrayList<CartItemsEntity>( list ) );
        }

        return cart;
    }

    @Override
    public CartItemsEntity toCartItemsEntity(CartItems cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        CartItemsEntity cartItemsEntity = new CartItemsEntity();

        cartItemsEntity.setId( cartItems.getId() );
        cartItemsEntity.setCart( cartItems.getCart() );
        cartItemsEntity.setProductId( cartItems.getProductId() );
        cartItemsEntity.setQuantity( cartItems.getQuantity() );

        return cartItemsEntity;
    }

    @Override
    public CartItems toCartItems(CartItemsEntity cartItemsEntity) {
        if ( cartItemsEntity == null ) {
            return null;
        }

        CartItems cartItems = new CartItems();

        cartItems.setId( cartItemsEntity.getId() );
        cartItems.setCart( cartItemsEntity.getCart() );
        cartItems.setProductId( cartItemsEntity.getProductId() );
        cartItems.setQuantity( cartItemsEntity.getQuantity() );

        return cartItems;
    }

    @Override
    public List<CartItems> toListCartItems(List<CartItemsEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<CartItems> list1 = new ArrayList<CartItems>( list.size() );
        for ( CartItemsEntity cartItemsEntity : list ) {
            list1.add( toCartItems( cartItemsEntity ) );
        }

        return list1;
    }
}
