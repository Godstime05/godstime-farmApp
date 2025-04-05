package com.godstime.ecommerce.farmsApp.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.godstime.ecommerce.farmsApp.auth.service.JWTUtils;
import com.godstime.ecommerce.farmsApp.dto.Request;
import com.godstime.ecommerce.farmsApp.model.User;
import com.godstime.ecommerce.farmsApp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public Request registerUser(Request request) {

        Request response = new Request();
        try {   

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setUsername(request.getUsername());

            User savedUser = userRepository.save(user);

            if (savedUser.getId()>0) {
                response.setStatusCode(200);                
                response.setMessage("User registered successfully");
                response.setUser(savedUser);
            }
            
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Internal Server Error");
            response.setMessage(e.getMessage());
           
        }
        return response;
    }

    public Request loginUser (Request request) {
        Request response = new Request();
        try {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()));
             var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
             var token = jwtUtils.generateToken(user);
             var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
             //response.setUser(user);
             response.setToken(token);
             response.setRefreshToken(refreshToken);
             response.setStatusCode(200);
             response.setRole(user.getRole());
             response.setExpirationTime("24 Hours");
             response.setMessage("User logged in successfully");
             
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public Request refreshToken (Request refreshTokenRequest) {
        Request response = new Request();   
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(email).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var token = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(token);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24 Hours");
                response.setMessage("Token refreshed successfully");
            }
            response.setStatusCode(200);
            return response;


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }

    }

    public Request getAllUsers() {
        Request response = new Request();
        try {
            List<User> users = userRepository.findAll();
            if(!users.isEmpty()) {  
                response.setUsersList(users);
                response.setStatusCode(200);
                response.setMessage("Users fetched successfully");
                
            } else {
                response.setStatusCode(404);
                response.setMessage("No users found");
            }
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
        
    }

    public Request getUserById(Long id) {
        Request response = new Request();
        try {
            User user = userRepository.findById(id).orElseThrow();
            response.setUser(user);
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;        
    }

    public Request getUserByEmail(String email) {
        Request response = new Request();
        try {
            User user = userRepository.findByEmail(email).orElseThrow();
            response.setUser(user);     
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request getUserByUsername(String username) {     
        Request response = new Request();
        try {
            User user = userRepository.findByUsername(username).orElseThrow();
            response.setUser(user);
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");   
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public Request getUserByPhoneNumber(String phoneNumber) {
        Request response = new Request();
        try {
            User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
            response.setUser(user);
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request deleteUser(Long id) {
        Request response = new Request();
        try {
            userRepository.deleteById(id);  
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request deleteUserByEmail(String email) {
        Request response = new Request();
        try {
            userRepository.deleteByEmail(email);    
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;            
    }

    public Request deleteUserByUsername(String username) {
        Request response = new Request();
        try {
            userRepository.deleteByUsername(username);          
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request deleteUserByPhoneNumber(String phoneNumber) {
        Request response = new Request();
        try {
            userRepository.deleteByPhoneNumber(phoneNumber);        
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request updateUser(Long id, Request request) {
        Request response = new Request();
        try {   
            User user = userRepository.findById(id).orElseThrow();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setUsername(request.getUsername());    
            userRepository.save(user);
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request updateUserByEmail(String email, Request request) {
        Request response = new Request();
        try {
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setUsername(request.getUsername());
            userRepository.save(user);
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }

    public Request updateUserByUsername(String username, Request request) {
        Request response = new Request();
        try {
            User user = userRepository.findByUsername(username).orElseThrow();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setUsername(request.getUsername());
            userRepository.save(user);
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;    
    }
}
