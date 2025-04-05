package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private List<CartItemResponse> cartItems;
    private BigDecimal totalPrice;

    @Data
    public static class CartItemResponse {
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