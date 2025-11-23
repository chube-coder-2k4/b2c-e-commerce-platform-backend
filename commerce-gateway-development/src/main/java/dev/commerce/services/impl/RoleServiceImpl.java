package dev.commerce.services.impl;

import dev.commerce.dtos.request.RoleRequest;
import dev.commerce.dtos.response.RoleResponse;
import dev.commerce.entitys.Role;
import dev.commerce.mappers.RoleMapper;
import dev.commerce.repositories.jpa.RoleRepository;
import dev.commerce.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> role = roleRepository.findAll();
        return role.stream().map(roleMapper::toResponse).toList();
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    @Override
    public RoleResponse updateRole(UUID id, RoleRequest role) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        Role saved = roleRepository.save(existingRole);
        return roleMapper.toResponse(saved);
    }

    @Override
    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponse getRoleById(UUID id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        return roleMapper.toResponse(role);
    }
}
