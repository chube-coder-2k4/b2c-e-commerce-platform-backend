package dev.commerce.dtos.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private double price;
    private int stockQuantity;
    private UUID categoryId;
}
