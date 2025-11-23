package dev.commerce.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RoleResponse {
    private UUID id;
    private String name;
}
