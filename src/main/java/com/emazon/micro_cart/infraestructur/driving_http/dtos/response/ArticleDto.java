package com.emazon.micro_cart.infraestructur.driving_http.dtos.response;

import com.emazon.micro_cart.domain.model.CategoryMod;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleDto {
     private String name;
    private String description;
    private Double price;
    private Integer quantityStock;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stockMessage;
    private Integer quantityInCart;
    private String brandName;
    private List<CategoryMod> categories; 
}
