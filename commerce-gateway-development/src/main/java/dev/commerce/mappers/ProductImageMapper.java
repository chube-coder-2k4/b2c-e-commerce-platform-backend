package dev.commerce.mappers;

import dev.commerce.dtos.request.ProductImageRequest;
import dev.commerce.dtos.response.ProductImageResponse;
import dev.commerce.entitys.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "id", ignore = true)
    ProductImage toEntity(ProductImageRequest request);

    @Mapping(source = "primary", target = "isPrimary")
    @Mapping(source = "product.id", target = "productId")
    ProductImageResponse toResponse(ProductImage entity);
}
