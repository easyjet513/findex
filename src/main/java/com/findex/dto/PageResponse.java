package com.findex.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final int totalElements;
    private final Long nextCursor;
    private final boolean hasNext;

    public PageResponse(List<T> content) {
        this.content = content;
        this.totalElements = content.size();
        this.nextCursor = null;
        this.hasNext = false;
    }

    public PageResponse(List<T> content, int totalElements, Long nextCursor, boolean hasNext) {
        this.content = content;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }
}