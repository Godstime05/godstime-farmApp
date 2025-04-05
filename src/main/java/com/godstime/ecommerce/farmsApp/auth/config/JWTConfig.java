package com.godstime.ecommerce.farmsApp.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Configuration
public class JWTConfig {

    @Bean
    public SecretKey secretKey() {
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}