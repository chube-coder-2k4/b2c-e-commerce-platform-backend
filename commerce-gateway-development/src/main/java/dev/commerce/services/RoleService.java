package dev.commerce.services;

import dev.commerce.dtos.request.RoleRequest;
import dev.commerce.dtos.response.RoleResponse;
import dev.commerce.entitys.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse createRole(RoleRequest role);
    RoleResponse updateRole(UUID id, RoleRequest role);
    void deleteRole(UUID id);
    RoleResponse getRoleById(UUID id);
}
