package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.dto.PaymentRequest;
import com.godstime.ecommerce.farmsApp.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializePayment(@RequestBody PaymentRequest request) {
        Map<String, Object> response = paymentService.initializePayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{transactionId}")
    public ResponseEntity<Map<String, Object>> verifyPayment(@PathVariable String transactionId) {
        Map<String, Object> response = paymentService.verifyPayment(transactionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> webhookPayload) {
        // TODO: Implement webhook handling logic
        // Verify webhook signature
        // Update order status
        // Send confirmation email
        return ResponseEntity.ok("Webhook received");
    }
} 