package com.transit.controller;

import com.transit.mapper.TokenMapper;
import com.transit.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenMapper tokenMapper;

    @GetMapping
    public Flux<Token> getAllTokens() {
        return Flux.fromIterable(tokenMapper.selectList(null));
    }

    @PostMapping
    public Mono<Token> createToken(@RequestBody Token token) {
        if (token.getKey() == null || token.getKey().isEmpty()) {
            token.setKey("sk-" + UUID.randomUUID().toString().replace("-", ""));
        }
        return Mono.fromCallable(() -> {
            tokenMapper.insert(token);
            return token;
        });
    }

    @PutMapping("/{id}")
    public Mono<Token> updateToken(@PathVariable Long id, @RequestBody Token token) {
        token.setId(id);
        return Mono.fromCallable(() -> {
            tokenMapper.updateById(token);
            return token;
        });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteToken(@PathVariable Long id) {
        return Mono.fromRunnable(() -> tokenMapper.deleteById(id));
    }
}
