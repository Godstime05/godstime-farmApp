package com.godstime.ecommerce.farmsApp.dto;

import com.godstime.ecommerce.farmsApp.model.Product;
import lombok.Data;
import java.util.List;
import java.math.BigDecimal;

@Data
public class SearchResponse {
    private List<Product> products;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<String> categories;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
} 