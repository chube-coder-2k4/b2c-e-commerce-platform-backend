package dev.commerce.services.impl;

import dev.commerce.dtos.request.ProductRequest;
import dev.commerce.dtos.response.ProductResponse;
import dev.commerce.entitys.Category;
import dev.commerce.entitys.Product;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.ProductMapper;
import dev.commerce.repositories.jpa.CategoryRepository;
import dev.commerce.repositories.jpa.ProductRepository;
import dev.commerce.services.ProductService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AuthenticationUtils utils;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductResponse> getAllProducts(String name, BigDecimal priceFrom, BigDecimal priceTo, int page, int size, String sortBy, String sortDir) {
        Specification<Product> spec = (root,query,cr) -> cr.isTrue(root.get("active"));
        if(name != null && !name.isEmpty()){
            spec = spec.and((root,query,cr) -> cr.like(cr.lower(root.get("name")), "%"+name.toLowerCase()+"%"));
        }
        if(priceFrom != null){
            spec = spec.and((root,query,cr) -> cr.greaterThanOrEqualTo(root.get("price"), priceFrom));
        }
        if(priceTo != null){
            spec = spec.and((root,query,cr) -> cr.lessThanOrEqualTo(root.get("price"), priceTo));
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id).filter(Product::isActive).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setCreatedBy(utils.getCurrentUserId());
        product.setActive(true);
        if(product.getSlug() == null || product.getSlug().isEmpty()){
            product.setSlug(slugify(product.getName()));
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id).filter(Product::isActive).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
        product.setName(request.getName());
        product.setSlug(request.getSlug() != null ? request.getSlug() : slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setUpdatedBy(utils.getCurrentUserId());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        product.setActive(false);
        productRepository.save(product);
    }

    private String slugify(String input) {
        return input.toLowerCase().trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
