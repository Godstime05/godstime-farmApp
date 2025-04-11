package com.godstime.ecommerce.farmsApp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger; //this isi the logger for the flutterwave payment service
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godstime.ecommerce.farmsApp.dto.PaymentRequest;
import com.godstime.ecommerce.farmsApp.service.PaymentService;
import com.godstime.ecommerce.farmsApp.services.OrderService;

@Service
public class FlutterwavePaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(FlutterwavePaymentServiceImpl.class);
    private static final String TRANSACTION_PREFIX = "GF-";

    private final RestTemplate restTemplate;
    private final String flutterwaveSecretKey;
    private final String flutterwaveBaseUrl;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public FlutterwavePaymentServiceImpl(
            RestTemplate restTemplate,
            OrderService orderService,
            @Value("${flutterwave.secretKey}") String flutterwaveSecretKey,
            @Value("${flutterwave.baseUrl}") String flutterwaveBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.orderService = orderService;
        this.flutterwaveSecretKey = flutterwaveSecretKey;
        this.flutterwaveBaseUrl = flutterwaveBaseUrl;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> initializePayment(PaymentRequest request) {
        logger.info("Initializing Flutterwave payment for amount: {} {}", request.getAmount(), request.getCurrency());
        String initializeUrl = flutterwaveBaseUrl + "/payments";
        HttpHeaders headers = createHeaders();

        Map<String, Object> payload = new HashMap<>();
        payload.put("tx_ref", generateTransactionReference());
        payload.put("amount", request.getAmount());
        payload.put("currency", request.getCurrency());
        payload.put("redirect_url", request.getRedirectUrl());
        payload.put("customer", createCustomerObject(request));
        payload.put("customizations", createCustomizationObject());
        
        if (request.getPaymentMethod() != null) {
            payload.put("payment_options", request.getPaymentMethod());
        }
        
        if (request.getDescription() != null) {
            payload.put("narration", request.getDescription());
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
        return restTemplate.postForObject(initializeUrl, requestEntity, Map.class);
    }

    @Override
    public Map<String, Object> verifyPayment(String transactionId) {
        logger.info("Verifying Flutterwave transaction: {}", transactionId);
        String verifyUrl = flutterwaveBaseUrl + "/transactions/" + transactionId + "/verify";
        HttpHeaders headers = createHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.getForObject(verifyUrl, Map.class, requestEntity);
    }

    @Override
    public void handleWebhook(String payload) {
        try {
            logger.info("Received Flutterwave webhook notification");
            
            // Parse the webhook payload
            Map<String, Object> webhookData = objectMapper.readValue(payload, Map.class);
            
            // Verify webhook signature
            String webhookSignature = webhookData.get("verif-hash").toString();
            if (!verifyWebhookSignature(webhookSignature)) {
                logger.error("Invalid webhook signature");
                return;
            }

            // Extract transaction data
            Map<String, Object> data = (Map<String, Object>) webhookData.get("data");
            String transactionId = data.get("id").toString();
            String status = data.get("status").toString();
            String txRef = data.get("tx_ref").toString();

            // Verify transaction status with Flutterwave
            Map<String, Object> verificationResponse = verifyPayment(transactionId);
            if (!"successful".equals(verificationResponse.get("status"))) {
                logger.error("Transaction verification failed for ID: {}", transactionId);
                return;
            }

            // Extract order ID from transaction reference
            String orderId = extractOrderIdFromTxRef(txRef);
            if (orderId == null) {
                logger.error("Invalid transaction reference format: {}", txRef);
                return;
            }

            // Update order status based on payment status
            switch (status.toLowerCase()) {
                case "successful":
                    orderService.updateOrderStatus(Long.parseLong(orderId), "PAID");
                    // TODO: Send confirmation email to customer
                    break;
                case "failed":
                    orderService.updateOrderStatus(Long.parseLong(orderId), "PAYMENT_FAILED");
                    break;
                case "cancelled":
                    orderService.updateOrderStatus(Long.parseLong(orderId), "CANCELLED");
                    break;
                default:
                    logger.warn("Unhandled payment status: {}", status);
            }

            logger.info("Successfully processed webhook for transaction: {}", transactionId);
        } catch (Exception e) {
            logger.error("Error processing webhook: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process webhook", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + flutterwaveSecretKey);
        return headers;
    }

    private String generateTransactionReference() {
        return TRANSACTION_PREFIX + System.currentTimeMillis();
    }

    private Map<String, Object> createCustomerObject(PaymentRequest request) {
        Map<String, Object> customer = new HashMap<>();
        customer.put("email", request.getEmail());
        customer.put("name", request.getName());
        customer.put("phone_number", request.getPhoneNumber());
        return customer;
    }

    private Map<String, Object> createCustomizationObject() {
        Map<String, Object> customizations = new HashMap<>();
        customizations.put("title", "Godstime Farms Payment");
        customizations.put("description", "Payment for products from Godstime Farms");
        customizations.put("logo", "https://godstimefarms.com/logo.png");
        return customizations;
    }

    private boolean verifyWebhookSignature(String receivedSignature) {
        return flutterwaveSecretKey.equals(receivedSignature);
    }

    private String extractOrderIdFromTxRef(String txRef) {
        try {
            String[] parts = txRef.split("-");
            if (parts.length >= 3) {
                return parts[2];
            }
        } catch (Exception e) {
            logger.error("Error extracting order ID from tx_ref: {}", txRef);
        }
        return null;
    }
} 