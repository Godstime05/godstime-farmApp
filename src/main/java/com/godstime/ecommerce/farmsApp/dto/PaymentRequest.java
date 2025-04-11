package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private BigDecimal amount;
    private String currency;
    private String email;
    private String name;
    private String phoneNumber;
    private String redirectUrl;
    private String description;
    private String orderId;  // Reference to the order being paid for
    private String paymentMethod;  // Optional payment method preference
} 