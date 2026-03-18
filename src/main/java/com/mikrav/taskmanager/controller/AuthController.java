package com.mikrav.taskmanager.controller;

import com.mikrav.taskmanager.model.dto.LoginRequest;
import com.mikrav.taskmanager.model.dto.LoginResponse;
import com.mikrav.taskmanager.model.dto.RegisterRequest;
import com.mikrav.taskmanager.model.dto.RegisterResponse;
import com.mikrav.taskmanager.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registrationInfo) {
        return authService.register(registrationInfo);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginInfo) {
        return authService.login(loginInfo);
    }
}
