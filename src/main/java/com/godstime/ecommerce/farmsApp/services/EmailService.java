package com.godstime.ecommerce.farmsApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            Context context = new Context();
            context.setVariables(variables);
            
            String htmlContent = templateEngine.process(templateName, context);
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendOrderConfirmationEmail(String to, String orderId, String customerName, double totalAmount) {
        Map<String, Object> variables = Map.of(
            "orderId", orderId,
            "customerName", customerName,
            "totalAmount", totalAmount
        );
        sendHtmlEmail(to, "Order Confirmation - Godstime Farms", "order-confirmation", variables);
    }

    public void sendOrderStatusUpdateEmail(String to, String orderId, String status) {
        Map<String, Object> variables = Map.of(
            "orderId", orderId,
            "status", status
        );
        sendHtmlEmail(to, "Order Status Update - Godstime Farms", "order-status-update", variables);
    }

    public void sendWelcomeEmail(String to, String name) {
        Map<String, Object> variables = Map.of(
            "name", name
        );
        sendHtmlEmail(to, "Welcome to Godstime Farms", "welcome", variables);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        Map<String, Object> variables = Map.of(
            "resetToken", resetToken
        );
        sendHtmlEmail(to, "Password Reset - Godstime Farms", "password-reset", variables);
    }
} 