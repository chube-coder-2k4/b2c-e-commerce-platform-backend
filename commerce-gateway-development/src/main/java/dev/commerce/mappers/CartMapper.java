package dev.commerce.mappers;

import dev.commerce.dtos.response.CartResponse;
import dev.commerce.entitys.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(source = "users.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    CartResponse toResponse(Cart cart);
}
