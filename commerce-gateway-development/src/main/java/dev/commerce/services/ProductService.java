package dev.commerce.services;

import dev.commerce.dtos.request.ProductRequest;
import dev.commerce.dtos.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {
    Page<ProductResponse> getAllProducts(String name, BigDecimal priceFrom, BigDecimal priceTo , int page, int size, String sortBy, String sortDir);
    ProductResponse getProductById(UUID id);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(UUID id, ProductRequest request);
    void deleteProductById(UUID id);

}
