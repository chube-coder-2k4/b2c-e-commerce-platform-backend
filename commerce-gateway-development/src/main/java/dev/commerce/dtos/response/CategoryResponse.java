package dev.commerce.dtos.response;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private UUID id;
    private String name;
    private String slug;
    private boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;

}
