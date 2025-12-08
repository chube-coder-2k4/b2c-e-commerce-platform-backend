package dev.commerce.controllers;


import dev.commerce.dtos.response.CartResponse;
import dev.commerce.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@Validated
@Slf4j
@Tag(name = "Cart Controller", description = "APIs for managing shopping cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart", description = "Adds a product to the shopping cart with specified quantity.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Item added to cart successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid product ID or quantity"),
            @ApiResponse (responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @RequestParam UUID productId,
            @RequestParam int quantity
            ) {
        log.info("Adding product {} with quantity {} to cart", productId, quantity);
        return ResponseEntity.ok(cartService.addToCart(productId, quantity));
    }

    @Operation(summary = "Get my cart", description = "Retrieves the current user's shopping cart.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/my-cart")
    public ResponseEntity<CartResponse> getMyCart() {
        log.info("Retrieving current user's cart" );
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @Operation(summary = "Clear cart", description = "Clears all items from the current user's shopping cart.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        log.info("Clearing current user's cart" );
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a specific item in the shopping cart.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Cart item quantity updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid cart item ID or quantity"),
            @ApiResponse (responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/update-quantity")
    public ResponseEntity<CartResponse> updateQuantity(
            @RequestParam UUID cartItemId,
            @RequestParam int newQty
    ) {
        log.info("Updating cart item {} to new quantity {}", cartItemId, newQty);
        return ResponseEntity.ok(cartService.updateQuantity(cartItemId, newQty));
    }

    @DeleteMapping("/remove-item")
    @Operation(summary = "Remove cart item", description = "Removes a specific item from the shopping cart.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Cart item removed successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid cart item ID"),
            @ApiResponse (responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<CartResponse> removeCartItem(
            @RequestParam UUID cartItemId
    ) {
        log.info("Removing cart item {}", cartItemId);
        return ResponseEntity.ok(cartService.removeCartItem(cartItemId));
    }
}
