package com.godstime.ecommerce.farmsApp.services;

import com.godstime.ecommerce.farmsApp.dto.ReviewDTO;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.model.Review;
import com.godstime.ecommerce.farmsApp.model.User;
import com.godstime.ecommerce.farmsApp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    public ReviewDTO createReview(Long productId, ReviewDTO reviewRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productService.getProductById(productId);

        if (reviewRepository.existsByProductIdAndUserId(productId, user.getId())) {
            throw new RuntimeException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        Review savedReview = reviewRepository.save(review);
        return mapToDTO(savedReview);
    }

    public List<ReviewDTO> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getUserReviews() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reviewRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own reviews");
        }

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        Review updatedReview = reviewRepository.save(review);
        return mapToDTO(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
} 