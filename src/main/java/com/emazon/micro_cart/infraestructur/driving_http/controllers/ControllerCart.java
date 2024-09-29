package com.emazon.micro_cart.infraestructur.driving_http.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.ItemDto;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.PaginItemsDto;
import com.emazon.micro_cart.infraestructur.driving_http.mappers.IItemsMapper;
import com.emazon.micro_cart.infraestructur.driving_http.mappers.PaginItemResponseMapper;
import com.emazon.micro_cart.infraestructur.util.ConstantsInfraestructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@Service
@RequestMapping("/cart")
@AllArgsConstructor
@Validated
public class ControllerCart{
    private final ICartServicePort serviceCart;
    private final IItemsMapper mapperItems;

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
    public ResponseEntity<?> addItemsToCart(@PathVariable(ConstantsInfraestructure.ID) @NotNull @Min(1) Integer id,
                                            @PathVariable(ConstantsInfraestructure.QUANTITY) @NotNull @Min(1) Integer quantity){
    return ResponseEntity.ok(serviceCart.addItemsToCart(id,quantity));
    }


    @Operation(summary = "Method for delet items to the cart", description = "This method allows you to delete items to the cart\n\n "
            +
            "rules:\n\n" + //
            "      -Every time an item is deleted it should be removed from the cart.\n\n" +
            "      -Only the customer role can delete items from the cart.\n\n" +
            "      -When removing an item from the cart, the last modification date would be required to be updated..\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "article delete with exit"),
            @ApiResponse(responseCode = "400", description = " Invalid parameter. Possible errors:\n\n" +
                    "    - `id`: Cannot be null\n\n" +
                    "    - `quantity`: Must be greater than zero.\n\n"),
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/delete/{id}/{quantity}")
    public ResponseEntity<?> deleteItemsToCart(@PathVariable("id") @NotNull Integer id,
                                               @PathVariable("quantity") @NotNull Integer quantity){
        
     return ResponseEntity.ok(serviceCart.deleteItemsToCart(id, quantity));    
    }

    @Operation(summary = "Method for get all items in cart", description = "This method list all articles that have addes in the cart\n\n "
    +
    "rules:\n\n" + //
    "      -It should be possible to parameterize whether I want to bring the items from the cart ordered ascending or descending by name..\n\n" +
    "      -I need to be able to filter by category name, and/or brand name.\n\n" +
    "      -This service must be paginated.\n\n" + //
    "      -must show along with the article, the quantity of stock available in stock.\n\n"+
    "      -If there is no stock of the item, it must be shown when that item is supplied.\n\n"+
    "      -The total price of the items must be calculated.)")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "list of articles paginated"),
    @ApiResponse(responseCode = "400", description = " Invalid parameter. Possible errors:\n\n" +
            "    - `invalid filtername, size, page, orderBy\n\n"),

    @ApiResponse(responseCode = "404", description = "    - 'Not cart found\n\n" +
            "   - 'Not cart found' "),
     @ApiResponse(responseCode = "409", description = "   - 'conflict'\n\n" +
            "   - 'conflict, not items added yet in the cart' ")
})
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/")
    public ResponseEntity<PaginItemsDto> getAllItemsToCart(
        @RequestParam(defaultValue = ConstantsInfraestructure.ZERO_STRING) Integer page,
        @RequestParam(defaultValue = ConstantsInfraestructure.TEN_STRING) Integer size,
        @RequestParam(defaultValue = ConstantsInfraestructure.ASEND) String order,
        @RequestParam(required = false) String filterBy,
        @RequestParam (required = false) String nameFilter ){
     return ResponseEntity.ok(PaginItemResponseMapper.INSTANCE.toDto(serviceCart.getAllIemsToCart(size, page, order, filterBy,nameFilter )));    
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/itemsId")
    public ResponseEntity<List<ItemDto>> getAllIdItems(){
        return ResponseEntity.ok(mapperItems.toItemDto(serviceCart.getAllItems()));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateCart(){
        return ResponseEntity.ok(serviceCart.updateCart());
    }

    
}

