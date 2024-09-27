package com.emazon.micro_cart.infraestructur.driving_http.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.infraestructur.driving_http.dtos.response.ArticleDto;
import java.util.List;

@Mapper(componentModel = ("spring"))
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);
    List<ArticleDto> toDtoList(List<ArticlesMod> articles);
}  

