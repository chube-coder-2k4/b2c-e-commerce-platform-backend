package dev.commerce.controllers;

import dev.commerce.dtos.request.UserFilterRequest;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.request.UserUpdateRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import dev.commerce.mappers.UsersMapper;
import dev.commerce.services.UserService;
import dev.commerce.utils.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@Tag(name = "User Management", description = "User management APIs")
@Validated
public class UserController {
    private final UserService userService;
    private final AuthenticationUtils utils;
    private final UsersMapper usersMapper;

    @Operation(summary = "Create new user", description = "Register a new user account with email verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email already exists"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody UserRequest request) 
            throws MessagingException, UnsupportedEncodingException {
        UUID userId = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "userId", userId,
                    "message", "User created successfully. Please verify your email."
                ));
    }

    @Operation(summary = "Get All User with Pagination", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestBody UserFilterRequest request) {
        return ResponseEntity.ok(userService.getAllUserWithFilter(request));
    }

    @Operation(summary = "Update infor user", description = "Update user information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok("User updated successfully");
    }

    @Operation(summary = "Delete user", description = "Delete user by ID - soft delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Get me", description = "Retrieve current authenticated user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UUID userId = utils.getCurrentUserId();
        Users user = userService.findById(userId);
        UserResponse response = usersMapper.toDto(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        Users user = userService.findById(id);
        UserResponse response = usersMapper.toDto(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update my profile", description = "Update current authenticated user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/me")
    public ResponseEntity<String> updateMyProfile(@Valid @RequestBody UserUpdateRequest request) {
        UUID userId = utils.getCurrentUserId();
        userService.updateUser(userId, request);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
