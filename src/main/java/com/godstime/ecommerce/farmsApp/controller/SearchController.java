package com.godstime.ecommerce.farmsApp.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.godstime.ecommerce.farmsApp.dto.SearchRequest;
import com.godstime.ecommerce.farmsApp.dto.SearchResponse;
import com.godstime.ecommerce.farmsApp.services.SearchService;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/products")
    public ResponseEntity<SearchResponse> searchProducts(@RequestBody SearchRequest request) {
        SearchResponse response = searchService.searchProducts(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<SearchResponse> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        SearchRequest request = new SearchRequest();
        request.setQuery(query);
        request.setCategory(category);
        request.setMinPrice(minPrice != null ? BigDecimal.valueOf(minPrice) : null);
        request.setMaxPrice(maxPrice != null ? BigDecimal.valueOf(maxPrice) : null);
        request.setSortBy(sortBy);
        request.setPage(page);
        request.setSize(size);

        SearchResponse response = searchService.searchProducts(request);
        return ResponseEntity.ok(response);
    }
} 