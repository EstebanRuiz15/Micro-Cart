package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import com.emazon.micro_cart.domain.model.PaginItems;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.PaginItemsDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-26T21:32:47-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.1.jar, environment: Java 17.0.12 (Eclipse Adoptium)"
)
public class PaginItemResponseMapperImpl implements PaginItemResponseMapper {

    private final ArticleMapper articleMapper = ArticleMapper.INSTANCE;

    @Override
    public PaginItemsDto toDto(PaginItems paginItems) {
        if ( paginItems == null ) {
            return null;
        }

        PaginItemsDto paginItemsDto = new PaginItemsDto();

        paginItemsDto.setItems( articleMapper.toDtoList( paginItems.getItems() ) );
        paginItemsDto.setCurrentPage( paginItems.getCurrentPage() );
        paginItemsDto.setSize( paginItems.getSize() );
        paginItemsDto.setTotalpages( paginItems.getTotalpages() );
        paginItemsDto.setTotalData( paginItems.getTotalData() );
        paginItemsDto.setTotalToPay( paginItems.getTotalToPay() );

        return paginItemsDto;
    }
}
