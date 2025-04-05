package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String paymentMethod;

    @Data
    public static class OrderItemResponse {
        private Long id;
        private ProductResponse product;
        private Integer quantity;
        private BigDecimal price;
    }

    @Data
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private String category;
    }
} 