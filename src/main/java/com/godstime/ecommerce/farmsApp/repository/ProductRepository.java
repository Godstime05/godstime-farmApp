package com.godstime.ecommerce.farmsApp.repository;

import com.godstime.ecommerce.farmsApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByAvailableTrue();
    List<Product> findByNameContainingIgnoreCase(String name);
} 