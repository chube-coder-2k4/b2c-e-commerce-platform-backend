package dev.commerce.services;

import dev.commerce.dtos.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductImageService {
    ProductImageResponse uploadProductImage(UUID productId, MultipartFile file, boolean isPrimary);

    ProductImageResponse deleteProductImage(UUID productId, UUID imageId);

    ProductImageResponse setPrimaryImage(UUID productId, UUID imageId);
}
