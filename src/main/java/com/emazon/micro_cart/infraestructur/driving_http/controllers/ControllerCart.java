package com.emazon.micro_cart.infraestructur.driving_http.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@Service
@RequestMapping("/cart")
@AllArgsConstructor
@Validated
public class ControllerCart{

    @PostMapping("/{id}/{quantity}")
    public ResponseEntity<?> addItemsToCart(@PathVariable("id") @NotNull Integer id,
                                            @PathVariable("quantity") @NotNull Integer quantity){
    return ResponseEntity.ok("ok");
    }
}

