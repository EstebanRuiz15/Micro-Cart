package com.emazon.micro_cart.infraestructur.driven_rp.adapter;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.emazon.micro_cart.domain.interfaces.ICartPersistance;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.util.ConstantsDomain;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.mapper.IMapperCartToEntity;
import com.emazon.micro_cart.infraestructur.driven_rp.persistence.IRepositoryCartJpa;
import com.emazon.micro_cart.infraestructur.security.jwt_configuration.JwtService;
import com.emazon.micro_cart.infraestructur.util.ConstantsInfraestructure;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartPersistanceImpl implements ICartPersistance {
    private final IRepositoryCartJpa repositoryJpa;
    private final IMapperCartToEntity mapper;
    private final JwtService jwtService;

    @Override
    public void addItemToCart(Cart cart, CartItems cartItems) {

        CartItemsEntity cartItemsEntity = mapper.toCartItemsEntity(cartItems);
        cartItemsEntity.setCart(mapper.toCartEntity(cart));
        cart.getItems().add(cartItemsEntity);
        cartItems.setCart(mapper.toCartEntity(cart));
        repositoryJpa.save(mapper.toCartEntity(cart));
    }

    @Override
    public Optional<Cart> findByUserId(Integer id) {
        return repositoryJpa.findByUserId(id)
                .map(mapper::toCart);
    };

    @Override
    public Integer getClientId() {

        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String jwt = request.getHeader(ConstantsInfraestructure.AUTHORIZATION);
        jwt = jwt.substring(ConstantsDomain.SEVEN);
        return jwtService.extractUserId(jwt);
    }

    @Override
    public void save(Cart cart){
        repositoryJpa.save(mapper.toCartEntity(cart));
    }

}
