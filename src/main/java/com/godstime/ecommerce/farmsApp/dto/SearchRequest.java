package com.godstime.ecommerce.farmsApp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SearchRequest {
    private String query;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy; // "price_asc", "price_desc", "name_asc", "name_desc", "newest"
    private Integer page = 0;
    private Integer size = 10;
} 