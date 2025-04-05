package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.dto.OrderRequest;
import com.godstime.ecommerce.farmsApp.dto.OrderResponse;
import com.godstime.ecommerce.farmsApp.model.Order;
import com.godstime.ecommerce.farmsApp.model.OrderItem;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(mapToOrderResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        List<Order> orders = orderService.getUserOrders();
        return ResponseEntity.ok(orders.stream()
            .map(this::mapToOrderResponse)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(mapToOrderResponse(order));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(mapToOrderResponse(order));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders.stream()
            .map(this::mapToOrderResponse)
            .collect(Collectors.toList()));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<OrderResponse>> getRecentOrders(
            @RequestParam(defaultValue = "5") int limit) {
        List<Order> orders = orderService.getRecentOrders(limit);
        return ResponseEntity.ok(orders.stream()
            .map(this::mapToOrderResponse)
            .collect(Collectors.toList()));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalSpent() {
        return ResponseEntity.ok(orderService.getTotalSpent());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        
        response.setOrderItems(order.getOrderItems().stream()
            .map(this::mapToOrderItemResponse)
            .collect(Collectors.toList()));
            
        return response;
    }

    private OrderResponse.OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setProduct(mapToProductResponse(orderItem.getProduct()));
        return response;
    }

    private OrderResponse.ProductResponse mapToProductResponse(Product product) {
        OrderResponse.ProductResponse response = new OrderResponse.ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCategory(product.getCategory());
        return response;
    }
}