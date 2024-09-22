package com.emazon.micro_cart.infraestructur.driven_rp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emazon.micro_cart.infraestructur.driven_rp.entity.CartEntity;
import java.util.Optional;

@Repository
public interface IRepositoryCartJpa extends JpaRepository<CartEntity,Integer> {
    Optional<CartEntity> findByUserId(Integer id);
}
