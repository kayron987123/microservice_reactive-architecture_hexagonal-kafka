package com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PagedResponse<T>(
        int page,
        int size,
        long totalElements,
        List<T> data
) {
}
