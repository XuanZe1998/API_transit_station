package com.transit.controller;

import com.transit.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Mono<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        return authService.register(request.getUsername(), request.getPassword(), request.getEmail());
    }

    @PostMapping("/login")
    public Mono<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
