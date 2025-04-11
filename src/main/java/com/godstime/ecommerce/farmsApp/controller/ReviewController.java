package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.dto.ReviewDTO;
import com.godstime.ecommerce.farmsApp.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDTO>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<Double> getProductAverageRating(@PathVariable Long productId) {
        Double averageRating = reviewService.getProductAverageRating(productId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/product/{productId}/count")
    public ResponseEntity<Long> getProductReviewCount(@PathVariable Long productId) {
        Long reviewCount = reviewService.getProductReviewCount(productId);
        return ResponseEntity.ok(reviewCount);
    }
} 