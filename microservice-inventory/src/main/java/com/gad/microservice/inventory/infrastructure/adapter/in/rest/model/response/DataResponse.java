package com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response;

import lombok.Builder;

@Builder
public record DataResponse<T>(
        int status,
        String message,
        T data,
        String timestamp
) {
}
