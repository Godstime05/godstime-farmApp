package com.godstime.ecommerce.farmsApp.controller;

import com.godstime.ecommerce.farmsApp.dto.ReviewDTO;
import com.godstime.ecommerce.farmsApp.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/products/{productId}")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long productId,
            @RequestBody ReviewDTO reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(productId, reviewRequest));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewDTO>> getUserReviews() {
        return ResponseEntity.ok(reviewService.getUserReviews());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewRequest) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
} 