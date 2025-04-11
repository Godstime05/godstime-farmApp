package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.model.Wishlist;
import com.godstime.ecommerce.farmsApp.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<Wishlist>> getUserWishlist() {
        return ResponseEntity.ok(wishlistService.getUserWishlist());
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<Wishlist> addToWishlist(@PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(productId));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId) {
        wishlistService.removeFromWishlist(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productId}/check")
    public ResponseEntity<Boolean> isInWishlist(@PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.isInWishlist(productId));
    }
} 