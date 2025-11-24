package dev.commerce.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import dev.commerce.dtos.response.ProductImageResponse;
import dev.commerce.entitys.Product;
import dev.commerce.entitys.ProductImage;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.ProductImageMapper;
import dev.commerce.repositories.jpa.ProductImageRepository;
import dev.commerce.repositories.jpa.ProductRepository;
import dev.commerce.services.ProductImageService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;
    private final AuthenticationUtils utils;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    @Transactional
    public ProductImageResponse uploadProductImage(UUID productId, MultipartFile file, boolean isPrimary) {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size (10MB)");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed");
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + productId)
        );

        Map<String, Object> uploadResult;
        try {
//            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//                    "folder", "products",
//                    "resource_type", "image",
//                    "transformation", ObjectUtils.asMap("quality", "auto")
//            ));
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "products",
                    "resource_type", "image",
                    "transformation", new Transformation().quality("auto") // dùng Transformation object
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }

        ProductImage productImage = ProductImage.builder()
                .id(UUID.randomUUID())
                .product(product)
                .imageUrl((String) uploadResult.get("secure_url"))
                .publicId((String) uploadResult.get("public_id"))
                .primary(isPrimary)
                .build();

        if (isPrimary) {
            productImageRepository.findByProductIdAndPrimaryTrue(productId)
                    .forEach(img -> {
                        img.setPrimary(false);
                        productImageRepository.save(img);
                    });
        }
        productImage.setCreatedBy(utils.getCurrentUserId());
        ProductImage saved = productImageRepository.save(productImage);
        return productImageMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductImageResponse deleteProductImage(UUID productId, UUID imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductImage image = productImageRepository.findById(imageId)
                .filter(img -> img.getProduct().getId().equals(product.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        try {
            cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
        productImageRepository.delete(image);

        return ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .productId(product.getId())
                .publicId(image.getPublicId())
                .isPrimary(image.isPrimary())
                .build();
    }

    @Override
    @Transactional
    public ProductImageResponse setPrimaryImage(UUID productId, UUID imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductImage image = productImageRepository.findById(imageId)
                .filter(img -> img.getProduct().getId().equals(product.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        productImageRepository.findByProductIdAndPrimaryTrue(productId)
                .forEach(img -> {
                    img.setPrimary(false);
                    productImageRepository.save(img);
                });

        image.setPrimary(true);
        productImageRepository.save(image);

        return productImageMapper.toResponse(image);
    }
}
