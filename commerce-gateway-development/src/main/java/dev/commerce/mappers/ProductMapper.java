package dev.commerce.mappers;

import dev.commerce.dtos.request.ProductRequest;
import dev.commerce.dtos.response.ProductResponse;
import dev.commerce.entitys.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponse(Product entity);
}
