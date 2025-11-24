package dev.commerce.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private UUID categoryId;
    private String categoryName;
    private boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;
}
