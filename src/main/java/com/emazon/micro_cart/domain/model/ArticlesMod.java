package com.emazon.micro_cart.domain.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ArticlesMod {
    private String name;
    private String description;
    private Double price;
    private Integer quantityStock;
    private String brandName;
    private List<CategoryMod> categories;

}
