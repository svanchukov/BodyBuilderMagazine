package com.example.BodybuilderMagazine.mappers;

import com.example.BodybuilderMagazine.dto.CreateNewProductDTO;
import com.example.BodybuilderMagazine.entity.ProductsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateNewProductMapper {

    ProductsEntity toEntity(CreateNewProductDTO createNewProductDTO);
}
