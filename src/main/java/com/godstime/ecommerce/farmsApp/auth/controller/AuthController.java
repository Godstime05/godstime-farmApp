package com.godstime.ecommerce.farmsApp.auth.controller;

import com.godstime.ecommerce.farmsApp.dto.Request;
import com.godstime.ecommerce.farmsApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }   

    @PostMapping("/auth/register")
    public ResponseEntity<Request> register(@RequestBody Request registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Request> login(@RequestBody Request request) {
        return ResponseEntity.ok(userService.loginUser(request));   
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Request> refreshToken(@RequestBody Request refreshTokenRequest) {
        return ResponseEntity.ok(userService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/admin/get-users")
    public ResponseEntity<Request> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/get-users/{id}")
    public ResponseEntity<Request> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

  

    @PutMapping("/admin/update-users/{id}")
    public ResponseEntity<Request> updateUser(@PathVariable Long id, @RequestBody Request updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/admin/delete-users/{id}")
    public ResponseEntity<Request> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }


}
