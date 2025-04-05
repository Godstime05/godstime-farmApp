package com.godstime.ecommerce.farmsApp.services;

import com.godstime.ecommerce.farmsApp.dto.CartItemRequest;
import com.godstime.ecommerce.farmsApp.model.*;
import com.godstime.ecommerce.farmsApp.repository.CartRepository;
import com.godstime.ecommerce.farmsApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public Cart getCart() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addToCart(CartItemRequest request) {
        Cart cart = getCart();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cart.getCartItems().add(newItem);
        }

        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeFromCart(Long productId) {
        Cart cart = getCart();
        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItemQuantity(Long productId, Integer quantity) {
        Cart cart = getCart();
        CartItem item = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
        } else {
            item.setQuantity(quantity);
            item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    public void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }

    public void clearCart() {
        Cart cart = getCart();
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public BigDecimal getCartTotal() {
        return getCart().getTotalPrice();
    }

    public Integer getCartItemCount() {
        return getCart().getCartItems().size();
    }
} 