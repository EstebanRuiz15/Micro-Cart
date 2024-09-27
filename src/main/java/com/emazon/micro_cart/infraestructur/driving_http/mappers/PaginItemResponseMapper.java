package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.emazon.micro_cart.domain.model.PaginItems;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.PaginItemsDto;

@Mapper(uses = ArticleMapper.class)
public interface PaginItemResponseMapper {
     PaginItemResponseMapper INSTANCE = Mappers.getMapper(PaginItemResponseMapper.class);

    PaginItemsDto toDto(PaginItems paginItems);
}
