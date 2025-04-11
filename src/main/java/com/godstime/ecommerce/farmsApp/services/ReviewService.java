package com.godstime.ecommerce.farmsApp.services;

import com.godstime.ecommerce.farmsApp.dto.ReviewDTO;
import com.godstime.ecommerce.farmsApp.model.Product;
import com.godstime.ecommerce.farmsApp.model.Review;
import com.godstime.ecommerce.farmsApp.model.User;
import com.godstime.ecommerce.farmsApp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        User user = userService.getCurrentUser();
        Product product = productService.getProductById(reviewDTO.getProductId());

        // Check if user has already reviewed this product
        if (reviewRepository.findByUserIdAndProductId(user.getId(), product.getId()).isPresent()) {
            throw new IllegalArgumentException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setVerifiedPurchase(checkIfVerifiedPurchase(user.getId(), product.getId()));

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        User currentUser = userService.getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        User currentUser = userService.getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    public Page<ReviewDTO> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable)
                .map(this::convertToDTO);
    }

    public Double getProductAverageRating(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    public Long getProductReviewCount(Long productId) {
        return reviewRepository.getReviewCountByProductId(productId);
    }

    private boolean checkIfVerifiedPurchase(Long userId, Long productId) {
        // TODO: Implement logic to check if user has purchased the product
        return false;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getUsername());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setVerifiedPurchase(review.getVerifiedPurchase());
        return dto;
    }
} 