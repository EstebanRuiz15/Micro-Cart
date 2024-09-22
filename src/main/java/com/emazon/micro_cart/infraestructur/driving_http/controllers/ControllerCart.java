package com.emazon.micro_cart.infraestructur.driving_http.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.infraestructur.util.ConstantsInfraestructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@Service
@RequestMapping("/cart")
@AllArgsConstructor
@Validated
public class ControllerCart{
    private final ICartServicePort serviceCart;

    @Operation(summary = "Method for added items to the cart", description = "This method allows you to add items to the cart, if you already have a cart add the item, if you don't have one create a cart and add your first item\n\n "
            +
            "rules:\n\n" + //
            "      -Each customer can only have one shopping cart.\n\n" +
            "      -The items of interest and the desired quantity must be added to the cart. You also want to save the last date the cart was modified.\n\n" +
            "      -It must be validated at the time of adding an article that the quantity exists in stock\n\n" + //
            "      -If there is no stock of the item, it must be shown when that item is supplied.\n\n"+
            "      -Only the customer role can add items to the cart\n\n"+
            "      -You can only have a maximum of 3 items per category (regardless of the quantity of the item)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "article added with exit"),
            @ApiResponse(responseCode = "400", description = " Invalid parameter. Possible errors:\n\n" +
                    "    - `id`: Cannot be null\n\n" +
                    "    - `quantity`: Must be greater than zero.\n\n"),

            @ApiResponse(responseCode = "404", description = "    - 'Not article found for this id'\n\n" +
                    "   - 'Not article found' "),
             @ApiResponse(responseCode = "409", description = "   - 'conflict, There cannot be more than 3 articles with the same category'\n\n" +
                    "   - 'conflict, at this moment only have # of articles' ")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{id}/{quantity}")
    public ResponseEntity<?> addItemsToCart(@PathVariable(ConstantsInfraestructure.ID) @NotNull Integer id,
                                            @PathVariable(ConstantsInfraestructure.QUANTITY) @NotNull Integer quantity){
    serviceCart.addItemsToCart(id,quantity);
    return ResponseEntity.ok(ConstantsInfraestructure.ADD_WITH_EXIT);
    }
}

