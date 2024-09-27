package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.model.CategoryMod;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.ArticleDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-26T20:56:12-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.1.jar, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public List<ArticleDto> toDtoList(List<ArticlesMod> articles) {
        if ( articles == null ) {
            return null;
        }

        List<ArticleDto> list = new ArrayList<ArticleDto>( articles.size() );
        for ( ArticlesMod articlesMod : articles ) {
            list.add( articlesModToArticleDto( articlesMod ) );
        }

        return list;
    }

    protected ArticleDto articlesModToArticleDto(ArticlesMod articlesMod) {
        if ( articlesMod == null ) {
            return null;
        }

        ArticleDto articleDto = new ArticleDto();

        articleDto.setName( articlesMod.getName() );
        articleDto.setDescription( articlesMod.getDescription() );
        articleDto.setPrice( articlesMod.getPrice() );
        articleDto.setQuantityStock( articlesMod.getQuantityStock() );
        articleDto.setStockMessage( articlesMod.getStockMessage() );
        articleDto.setQuantityInCart( articlesMod.getQuantityInCart() );
        articleDto.setBrandName( articlesMod.getBrandName() );
        List<CategoryMod> list = articlesMod.getCategories();
        if ( list != null ) {
            articleDto.setCategories( new ArrayList<CategoryMod>( list ) );
        }

        return articleDto;
    }
}
