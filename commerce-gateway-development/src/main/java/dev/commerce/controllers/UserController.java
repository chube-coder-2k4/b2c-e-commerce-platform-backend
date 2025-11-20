package dev.commerce.controllers;

import dev.commerce.dtos.request.UserRequest;
import dev.commerce.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Create new user", description = "Register a new user account with email verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email already exists"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody UserRequest request) 
            throws MessagingException, UnsupportedEncodingException {
        UUID userId = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "userId", userId,
                    "message", "User created successfully. Please verify your email."
                ));
    }
}
