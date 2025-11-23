package dev.commerce.controllers;

import dev.commerce.dtos.request.RoleRequest;
import dev.commerce.dtos.response.RoleResponse;
import dev.commerce.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/role")
@Slf4j
@Tag(name = "Role Controller", description = "APIs for managing roles")
public class RoleController {
    private final RoleService roleService;
    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles in the system")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of roles"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        log.info("Request to get all roles");
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieve a role by its unique ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved role"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        log.info("Request to get role by ID");
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role by ID", description = "Delete a role by its unique ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted role"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteRoleById(@PathVariable UUID id) {
        log.info("Request to delete role by ID");
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    @Operation(summary = "Update role", description = "Update an existing role by its unique ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated role"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID id, @RequestBody RoleRequest
            roleName) {
        log.info("Request to update role");
        return ResponseEntity.ok(roleService.updateRole(id, roleName));
    }


    @PostMapping("/")
    @Operation(summary = "Create new role", description = "Create a new role in the system")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Successfully created role"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        log.info("Request to create new role");
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

}
