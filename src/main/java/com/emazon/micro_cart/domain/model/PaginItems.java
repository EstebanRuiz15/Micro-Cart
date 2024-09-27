package com.emazon.micro_cart.domain.model;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class PaginItems {
    private List<ArticlesMod> items;
    private  Integer  currentPage;
    private  Integer size;
    private  Integer totalpages;
    private  Integer totalData;
    private String totalToPay;
}
