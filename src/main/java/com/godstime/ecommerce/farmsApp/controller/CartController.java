package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.dto.CartItemRequest;
import com.godstime.ecommerce.farmsApp.dto.CartResponse;
import com.godstime.ecommerce.farmsApp.model.Cart;
import com.godstime.ecommerce.farmsApp.model.CartItem;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Cart cart = cartService.getCart();
        return ResponseEntity.ok(mapToCartResponse(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartItemRequest request) {
        Cart cart = cartService.addToCart(request);
        return ResponseEntity.ok(mapToCartResponse(cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long productId) {
        Cart cart = cartService.removeFromCart(productId);
        return ResponseEntity.ok(mapToCartResponse(cart));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Cart cart = cartService.updateCartItemQuantity(productId, quantity);
        return ResponseEntity.ok(mapToCartResponse(cart));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getCartTotal() {
        return ResponseEntity.ok(cartService.getCartTotal());
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount() {
        return ResponseEntity.ok(cartService.getCartItemCount());
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setTotalPrice(cart.getTotalPrice());
        
        response.setCartItems(cart.getCartItems().stream()
            .map(this::mapToCartItemResponse)
            .collect(Collectors.toList()));
            
        return response;
    }

    private CartResponse.CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartResponse.CartItemResponse response = new CartResponse.CartItemResponse();
        response.setId(cartItem.getId());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());
        response.setProduct(mapToProductResponse(cartItem.getProduct()));
        return response;
    }

    private CartResponse.ProductResponse mapToProductResponse(Product product) {
        CartResponse.ProductResponse response = new CartResponse.ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCategory(product.getCategory());
        return response;
    }
} 