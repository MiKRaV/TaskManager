package com.mikrav.taskmanager.service;

import com.mikrav.taskmanager.model.dto.LoginRequest;
import com.mikrav.taskmanager.model.dto.LoginResponse;
import com.mikrav.taskmanager.model.dto.RegisterRequest;
import com.mikrav.taskmanager.model.dto.RegisterResponse;
import com.mikrav.taskmanager.model.entity.User;
import com.mikrav.taskmanager.model.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userService.create(user);
        return RegisterResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        User user = userService.getByUsername(loginRequest.getUsername());
        jwtService.generateToken(user);
        String jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken);
    }
}
