package dev.commerce.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
public class RoleRequest {
    @NotBlank
    private String name;

    private String description;
}
