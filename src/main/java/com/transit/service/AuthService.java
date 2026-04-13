package com.transit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.transit.mapper.UserMapper;
import com.transit.model.User;
import com.transit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public Mono<Map<String, Object>> register(String username, String password, String email) {
        return Mono.fromCallable(() -> {
            if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) != null) {
                throw new RuntimeException("User already exists");
            }
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .role("USER")
                    .createdAt(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
            
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("username", user.getUsername());
            result.put("role", user.getRole());
            return result;
        });
    }

    public Mono<Map<String, Object>> login(String username, String password) {
        return Mono.fromCallable(() -> {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
            if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }
            
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("username", user.getUsername());
            result.put("role", user.getRole());
            return result;
        });
    }
}
