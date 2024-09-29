package com.emazon.micro_cart.infraestructur.driven_rp.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartItemsEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IRepositoryCartItemsJpa extends JpaRepository<CartItemsEntity, Integer> {
    @Query("SELECT ci.productId FROM CartItemsEntity ci WHERE ci.cart.id = :cartId")
    List<Integer> findItemIdsByCartId(@Param("cartId") Integer cartId);

    @Query("SELECT SUM(c.quantity) FROM CartItemsEntity c WHERE c.cart.userId = :userId AND c.productId = :productId")
    Integer findTotalQuantityByClientAndArticle(@Param("userId") Integer clientId, @Param("productId") Integer productId);

    @Modifying
    @Query("DELETE FROM CartItemsEntity c WHERE c.cart.userId = :userId AND c.productId = :productId")
    void deleteAllByClientAndProduct(@Param("userId") Integer clientId, @Param("productId") Integer productId);

    @Query("SELECT ci FROM CartItemsEntity ci WHERE ci.productId = :productId AND ci.cart.userId = :userId")
    Optional<CartItemsEntity> findByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    List<CartItemsEntity> findByCartId(Integer id);

    @Query("SELECT ci.productId FROM CartItemsEntity ci WHERE ci.cart.id = :cartId")
    Page<Long> findProductIdsByCartId(@Param("cartId") Long cartId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemsEntity i WHERE i.cart.id = :cartId")
    void deleteByCartId(Integer cartId);
}
