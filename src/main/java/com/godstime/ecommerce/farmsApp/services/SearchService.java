package com.godstime.ecommerce.farmsApp.services;

import com.godstime.ecommerce.farmsApp.dto.SearchRequest;
import com.godstime.ecommerce.farmsApp.dto.SearchResponse;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    public SearchResponse searchProducts(SearchRequest request) {
        // Create page request with sorting
        PageRequest pageRequest = createPageRequest(request);

        // Search products with filters
        Page<Product> productPage = productRepository.searchProducts(
            request.getQuery(),
            request.getCategory(),
            request.getMinPrice(),
            request.getMaxPrice(),
            pageRequest
        );

        // Get all categories for filter options
        List<String> categories = productRepository.findAllCategories();

        // Get price range for filter options
        BigDecimal minPrice = productRepository.findMinPrice();
        BigDecimal maxPrice = productRepository.findMaxPrice();

        // Create response
        SearchResponse response = new SearchResponse();
        response.setProducts(productPage.getContent());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setCurrentPage(request.getPage());
        response.setPageSize(request.getSize());
        response.setCategories(categories);
        response.setMinPrice(minPrice);
        response.setMaxPrice(maxPrice);

        return response;
    }

    private PageRequest createPageRequest(SearchRequest request) {
        Sort sort = createSort(request.getSortBy());
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private Sort createSort(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.unsorted();
        }

        switch (sortBy.toLowerCase()) {
            case "price_asc":
                return Sort.by("price").ascending();
            case "price_desc":
                return Sort.by("price").descending();
            case "name_asc":
                return Sort.by("name").ascending();
            case "name_desc":
                return Sort.by("name").descending();
            case "newest":
                return Sort.by("createdAt").descending();
            default:
                return Sort.unsorted();
        }
    }
} 