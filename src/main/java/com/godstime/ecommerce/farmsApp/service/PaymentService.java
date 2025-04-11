package com.godstime.ecommerce.farmsApp.service;

import com.godstime.ecommerce.farmsApp.dto.PaymentRequest;
import java.util.Map;

public interface PaymentService {
    /**
     * Initializes a Flutterwave payment transaction
     * @param request Payment details
     * @return Flutterwave payment response
     */
    Map<String, Object> initializePayment(PaymentRequest request);

    /**
     * Verifies a Flutterwave transaction
     * @param transactionId Flutterwave transaction ID
     * @return Transaction verification response
     */
    Map<String, Object> verifyPayment(String transactionId);

    /**
     * Handles Flutterwave webhook notifications
     * @param payload Webhook payload from Flutterwave
     */
    void handleWebhook(String payload);
} 