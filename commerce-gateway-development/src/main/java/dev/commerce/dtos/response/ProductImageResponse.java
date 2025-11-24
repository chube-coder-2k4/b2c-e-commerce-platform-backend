package dev.commerce.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {
    private UUID id;
    private String imageUrl;
    private UUID productId;
    private String publicId;
    private boolean isPrimary;
    private UUID createdBy;
    private UUID updatedBy;
}
