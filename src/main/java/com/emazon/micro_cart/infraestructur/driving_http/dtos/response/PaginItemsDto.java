package com.emazon.micro_cart.infraestructur.driving_http.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class PaginItemsDto {
    private List<ArticleDto> items; 
    private Integer currentPage;
    private Integer size;
    private Integer totalpages;
    private Integer totalData;
    private String totalToPay;
}
