package com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response;

import lombok.Builder;

@Builder
public record DataResponse(
        int status,
        String message,
        Object data,
        String timestamp
) {
}
