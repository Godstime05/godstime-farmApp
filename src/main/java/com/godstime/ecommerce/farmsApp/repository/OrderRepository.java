package com.godstime.ecommerce.farmsApp.repository;

import com.godstime.ecommerce.farmsApp.model.Order;
import com.godstime.ecommerce.farmsApp.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
    List<Order> findTopByUserOrderByOrderDateDesc(User user, Pageable pageable);
} 