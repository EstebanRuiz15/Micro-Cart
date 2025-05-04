package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.ItemDto;

@Mapper(componentModel = "spring")
public interface IItemsMapper {

    List<ItemDto> toItemDto(List<CartItems> items);
}
