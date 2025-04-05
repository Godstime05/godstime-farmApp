package com.godstime.ecommerce.farmsApp.dto;

import java.util.List;

import com.godstime.ecommerce.farmsApp.model.User;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    
    private String username;
    private String email;
    private String password;
    private String role;
    private String phoneNumber;

    private User user;
    private List<User> UsersList;
    
}
