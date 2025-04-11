package com.godstime.ecommerce.farmsApp.repository;

import com.godstime.ecommerce.farmsApp.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    Page<Review> findByProductId(Long productId, Pageable pageable);
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Long getReviewCountByProductId(@Param("productId") Long productId);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.verifiedPurchase = true")
    List<Review> findVerifiedReviewsByProductId(@Param("productId") Long productId);
} 