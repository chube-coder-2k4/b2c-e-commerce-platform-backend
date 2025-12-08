package dev.commerce.mappers;

import dev.commerce.dtos.response.CartItemResponse;
import dev.commerce.entitys.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface CartItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName" )
    @Mapping(source = "product.price", target = "totalPrice" )
    @Mapping(source = "createdBy", target = "createdBy" )
    @Mapping(source = "updatedBy", target = "updatedBy" )
    CartItemResponse toResponse(CartItem cartItem);
}
