package com.godstime.ecommerce.farmsApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(10000);   // 10 seconds
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // You can add custom error handlers or interceptors here if needed
        // restTemplate.setErrorHandler(new CustomErrorHandler());
        // restTemplate.getInterceptors().add(new CustomInterceptor());
        
        return restTemplate;
    }
} 