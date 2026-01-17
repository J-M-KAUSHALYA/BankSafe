package com.edutech.progressive.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.edutech.progressive.dto.LoginRequest;
import com.edutech.progressive.entity.Customers;

public class CustomerLoginController {

    public ResponseEntity<Customers> registerUser(Customers user) {
        return null;
    }

    public ResponseEntity loginUser(LoginRequest loginRequest) {
        return null;
    }
}