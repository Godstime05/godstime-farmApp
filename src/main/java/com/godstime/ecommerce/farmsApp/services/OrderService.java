package com.godstime.ecommerce.farmsApp.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.godstime.ecommerce.farmsApp.dto.OrderRequest;
import com.godstime.ecommerce.farmsApp.model.Cart;
import com.godstime.ecommerce.farmsApp.model.CartItem;
import com.godstime.ecommerce.farmsApp.model.Order;
import com.godstime.ecommerce.farmsApp.model.OrderItem;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.model.User;
import com.godstime.ecommerce.farmsApp.repository.OrderRepository;
import com.godstime.ecommerce.farmsApp.repository.ProductRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Cart cart = cartService.getCart();
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTotalPrice(cart.getTotalPrice());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            // Update product stock
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            order.getOrderItems().add(orderItem);
        }

        // Clear the cart
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartService.updateCartTotal(cart);

        Order savedOrder = orderRepository.save(order);

        // Send order confirmation email
        emailService.sendOrderConfirmationEmail(
            user.getEmail(),
            savedOrder.getId().toString(),
            user.getUsername(),
            savedOrder.getTotalPrice().doubleValue()
        );

        return savedOrder;
    }

    public List<Order> getUserOrders() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUser(user);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Send order status update email
        emailService.sendOrderStatusUpdateEmail(
            order.getUser().getEmail(),
            order.getId().toString(),
            status
        );

        return updatedOrder;
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getRecentOrders(int limit) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findTopByUserOrderByOrderDateDesc(user, PageRequest.of(0, limit));
    }

    public BigDecimal getTotalSpent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 