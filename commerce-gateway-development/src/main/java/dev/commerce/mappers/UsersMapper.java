package dev.commerce.mappers;

import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", expression = "java(userRequest.getProvider() != null ? dev.commerce.dtos.common.LoginType.valueOf(userRequest.getProvider()) : dev.commerce.dtos.common.LoginType.LOCAL)")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isVerify", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isLocked", constant = "false")
    @Mapping(target = "password", ignore = true)
    Users toEntity(UserRequest userRequest);

    UserResponse toDto(Users entity);
}
