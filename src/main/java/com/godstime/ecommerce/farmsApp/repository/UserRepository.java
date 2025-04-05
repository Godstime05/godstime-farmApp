package com.godstime.ecommerce.farmsApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godstime.ecommerce.farmsApp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    void deleteByEmail(String email);
    void deleteByUsername(String username);
    void deleteByPhoneNumber(String phoneNumber);

}
