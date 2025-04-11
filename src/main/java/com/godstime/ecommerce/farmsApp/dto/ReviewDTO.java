package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long productId;
    private String productName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean verifiedPurchase;
}

@Data
class ReviewRequest {
    private Integer rating;
    private String comment;
} 