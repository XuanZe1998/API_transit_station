package com.transit.controller;

import com.transit.mapper.ChannelMapper;
import com.transit.model.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelMapper channelMapper;

    @GetMapping
    public Flux<Channel> getAllChannels() {
        return Flux.fromIterable(channelMapper.selectList(null));
    }

    @PostMapping
    public Mono<Channel> createChannel(@RequestBody Channel channel) {
        return Mono.fromCallable(() -> {
            channelMapper.insert(channel);
            return channel;
        });
    }

    @PutMapping("/{id}")
    public Mono<Channel> updateChannel(@PathVariable Long id, @RequestBody Channel channel) {
        channel.setId(id);
        return Mono.fromCallable(() -> {
            channelMapper.updateById(channel);
            return channel;
        });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteChannel(@PathVariable Long id) {
        return Mono.fromRunnable(() -> channelMapper.deleteById(id));
    }
}
