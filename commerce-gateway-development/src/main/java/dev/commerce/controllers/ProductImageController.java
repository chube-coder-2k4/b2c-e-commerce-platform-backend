package dev.commerce.controllers;

import dev.commerce.dtos.response.ProductImageResponse;
import dev.commerce.services.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/images")
@RequiredArgsConstructor
@Tag(name = "Product Images", description = "Manage product images")
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Upload product image")
    public ResponseEntity<ProductImageResponse> upload(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false") boolean isPrimary
    ) {
        return ResponseEntity.ok(productImageService.uploadProductImage(productId, file, isPrimary));
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "Delete product image")
    public ResponseEntity<ProductImageResponse> delete(
            @PathVariable UUID productId,
            @PathVariable UUID imageId
    ) {
        return ResponseEntity.ok(productImageService.deleteProductImage(productId, imageId));
    }

    @PutMapping("/{imageId}/primary")
    @Operation(summary = "Set primary image")
    public ResponseEntity<ProductImageResponse> setPrimary(
            @PathVariable UUID productId,
            @PathVariable UUID imageId
    ) {
        return ResponseEntity.ok(productImageService.setPrimaryImage(productId, imageId));
    }
}
