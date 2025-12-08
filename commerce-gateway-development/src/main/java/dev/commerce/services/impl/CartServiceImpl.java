package dev.commerce.services.impl;

import dev.commerce.dtos.response.CartResponse;
import dev.commerce.entitys.Cart;
import dev.commerce.entitys.CartItem;
import dev.commerce.entitys.Product;
import dev.commerce.entitys.Users;
import dev.commerce.mappers.CartMapper;
import dev.commerce.repositories.jpa.CartItemRepository;
import dev.commerce.repositories.jpa.CartRepository;
import dev.commerce.repositories.jpa.ProductRepository;
import dev.commerce.services.CartService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthenticationUtils utils;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse addToCart(UUID productId, int quantity) {
        Users user = utils.getCurrentUser();
        Cart cart = cartRepository.findByUsers(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUsers(user);
            return cartRepository.save(newCart);
        });
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        CartItem cartItem = new CartItem();
        if(existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
        }

        cartItemRepository.save(cartItem);
        double totalPrice = cartItemRepository.sumTotalPrice(cart.getId());
        cart.setTotalPrice(totalPrice);
        cart.setUsers(user);
        cart.setCreatedBy(user.getId());
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateQuantity(UUID cartItemId, int newQty) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if(newQty <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Product product = cartItem.getProduct();
        if(newQty > product.getStockQuantity()){
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }
        cartItem.setQuantity(newQty);
        cartItem.setUpdatedBy(utils.getCurrentUserId());
        cartItemRepository.save(cartItem);
        Cart cart = cartItem.getCart();
        Double totalPrice = cartItemRepository.sumTotalPrice(cart.getId());
        cart.setTotalPrice(totalPrice != null ? totalPrice : 0.0);
        cart.setUpdatedBy(utils.getCurrentUserId());
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse removeCartItem( UUID cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);
        double totalPrice = cartItemRepository.sumTotalPrice(cart.getId());
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getMyCart() {
        Users user = utils.getCurrentUser();
        Cart cart = cartRepository.findByUsers(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        return cartMapper.toResponse(cart);
    }

    @Override
    public void clearCart() {
        Users user = utils.getCurrentUser();
        Cart cart = cartRepository.findByUsers(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}
