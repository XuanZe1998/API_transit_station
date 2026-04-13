package com.transit.controller;

import com.transit.dto.PageResponse;
import com.transit.dto.PublicModel;
import com.transit.mapper.ChannelMapper;
import com.transit.mapper.ModelMappingMapper;
import com.transit.model.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final ModelMappingMapper modelMappingMapper;
    private final ChannelMapper channelMapper;

    @GetMapping("/models")
    public Mono<PageResponse<PublicModel>> models(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "12") int size,
                                                  @RequestParam(value = "query", required = false) String query,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "sort", required = false, defaultValue = "name") String sort) {
        if (page < 1) page = 1;
        if (size < 1) size = 12;
        int offset = (page - 1) * size;

        Long total = modelMappingMapper.countPublicModels(query,type);
        List<PublicModel> items;
        if ("hot".equalsIgnoreCase(sort)) {
            items = modelMappingMapper.findPublicModelsPagedHot(query, type, size, offset);
        } else if ("recent".equalsIgnoreCase(sort)) {
            items = modelMappingMapper.findPublicModelsPagedRecent(query, type, size, offset);
        } else {
            items = modelMappingMapper.findPublicModelsPagedByName(query, type, size, offset);
        }

        // Fallback: if no rows in model_mappings, derive from channels.models
        if ((total == null || total == 0) && (items == null || items.isEmpty())) {
            List<Channel> channels = channelMapper.selectList(null);
            Map<String, String> uniq = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            for (Channel c : channels) {
                if (c.isEnabled() && c.getModels() != null && !c.getModels().isEmpty()) {
                    String t = c.getType() == null ? "" : c.getType().toLowerCase(Locale.ROOT);
                    Arrays.stream(c.getModels().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .forEach(m -> uniq.putIfAbsent(m, t));
                }
            }
            List<PublicModel> all = new ArrayList<>();
            uniq.forEach((name, tp) -> {
                if ((query == null || name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                        && (type == null || type.equalsIgnoreCase(tp))) {
                    PublicModel pm = new PublicModel();
                    pm.setPublicName(name);
                    pm.setType(tp);
                    all.add(pm);
                }
            });
            if ("name".equalsIgnoreCase(sort)) {
                all.sort(Comparator.comparing(PublicModel::getPublicName, String.CASE_INSENSITIVE_ORDER));
            }
            int from = Math.min(offset, all.size());
            int to = Math.min(from + size, all.size());
            items = all.subList(from, to);
            total = (long) all.size();
        }

        PageResponse<PublicModel> resp = new PageResponse<>();
        resp.setTotal(total == null ? 0 : total);
        resp.setPage(page);
        resp.setSize(size);
        resp.setItems(items);
        return Mono.just(resp);
    }
}
