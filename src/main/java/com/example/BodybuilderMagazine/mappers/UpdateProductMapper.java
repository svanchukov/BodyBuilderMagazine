package com.example.BodybuilderMagazine.mappers;

import com.example.BodybuilderMagazine.dto.UpdateProductDTO;
import com.example.BodybuilderMagazine.entity.ProductsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateProductMapper {

    ProductsEntity toEntity(UpdateProductDTO updateProductDTO);
}
