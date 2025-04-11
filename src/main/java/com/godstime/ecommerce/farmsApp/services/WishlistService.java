package com.godstime.ecommerce.farmsApp.services;

import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.model.User;
import com.godstime.ecommerce.farmsApp.model.Wishlist;
import com.godstime.ecommerce.farmsApp.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductService productService;

    public List<Wishlist> getUserWishlist() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wishlistRepository.findByUserId(user.getId());
    }

    public Wishlist addToWishlist(Long productId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductById(productId);

        // Check if product is already in wishlist
        if (wishlistRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("Product is already in your wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(Long productId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        wishlistRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    public boolean isInWishlist(Long productId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wishlistRepository.existsByUserIdAndProductId(user.getId(), productId);
    }
} 