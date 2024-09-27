package com.emazon.micro_cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ArticlesMod {
    private Integer id;
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
