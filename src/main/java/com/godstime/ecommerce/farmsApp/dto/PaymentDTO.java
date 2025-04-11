package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private String transactionReference;
    private Long orderId;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String redirectUrl;
}

@Data
class PaymentResponse {
    private String transactionId;
    private String status;
    private String paymentMethod;
    private String redirectUrl;
    private String message;
} 