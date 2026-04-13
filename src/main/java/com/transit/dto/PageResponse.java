package com.transit.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private long total;
    private int page;
    private int size;
    private List<T> items;
}

