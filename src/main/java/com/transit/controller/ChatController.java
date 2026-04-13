package com.transit.controller;

import com.transit.dto.ChatRequest;
import com.transit.dto.ChatResponse;
import com.transit.service.TransitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ChatController {

    private final TransitService transitService;

    @PostMapping("/chat/completions")
    public Mono<ChatResponse> chatCompletions(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                                              @RequestBody ChatRequest request) {
        return transitService.chatCompletions(authorization, request);
    }
}
