package com.library.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.auth.DTO.AuthResponse;
import com.library.auth.DTO.UserLogin;
import com.library.auth.DTO.UserRegister;
import com.library.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User registration and login")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(description = "Creates a new user account with role USER")
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody UserRegister ur) {
        return authService.register(ur);
    }

    @Operation(description = "Authenticates user and returns JWT token")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserLogin ulg) {
        return authService.login(ulg);
    }

    @Operation(description = "Internal endpoint used by member-service")
    @GetMapping("/users/{id}/exists")
    public boolean userExists(@PathVariable String id) {
        return authService.userExists(id);
    }
}
