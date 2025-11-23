package dev.commerce.mappers;

import dev.commerce.dtos.request.RoleRequest;
import dev.commerce.dtos.response.RoleResponse;
import dev.commerce.entitys.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "id", ignore = true)
    Role toEntity(RoleRequest roleRequest);
    RoleResponse toResponse(Role role);
}
