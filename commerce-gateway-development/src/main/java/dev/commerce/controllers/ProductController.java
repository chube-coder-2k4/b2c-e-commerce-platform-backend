package dev.commerce.controllers;

import dev.commerce.dtos.request.ProductRequest;
import dev.commerce.dtos.response.ProductResponse;
import dev.commerce.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a paginated list of products with optional filtering and sorting.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/search")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) BigDecimal priceFrom,
            @RequestParam (required = false) BigDecimal priceTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                productService.getAllProducts(name, priceFrom, priceTo, page, size, sortBy, sortDir)
        );
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its unique identifier.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id)
    {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Delete product by ID", description = "Delete a product by its unique identifier.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create a new product", description = "Create a new product with the provided details.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the product"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Update an existing product", description = "Update the details of an existing product by its unique identifier.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the product"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }
}
