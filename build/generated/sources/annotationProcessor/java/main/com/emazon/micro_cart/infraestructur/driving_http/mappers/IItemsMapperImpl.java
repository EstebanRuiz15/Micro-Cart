package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.ItemDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-28T15:55:59-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.1.jar, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class IItemsMapperImpl implements IItemsMapper {

    @Override
    public List<ItemDto> toItemDto(List<CartItems> items) {
        if ( items == null ) {
            return null;
        }

        List<ItemDto> list = new ArrayList<ItemDto>( items.size() );
        for ( CartItems cartItems : items ) {
            list.add( cartItemsToItemDto( cartItems ) );
        }

        return list;
    }

    protected ItemDto cartItemsToItemDto(CartItems cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        ItemDto itemDto = new ItemDto();

        itemDto.setProductId( cartItems.getProductId() );
        itemDto.setQuantity( cartItems.getQuantity() );

        return itemDto;
    }
}
