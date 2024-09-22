package com.emazon.micro_cart.infraestructur.driven_rp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryCartItemsJpa extends JpaRepository<CartItemsEntity, Integer> {
    @Query("SELECT ci.productId FROM CartItemsEntity ci WHERE ci.cart.id = :cartId")
    List<Integer> findItemIdsByCartId(@Param("cartId") Integer cartId);
}
