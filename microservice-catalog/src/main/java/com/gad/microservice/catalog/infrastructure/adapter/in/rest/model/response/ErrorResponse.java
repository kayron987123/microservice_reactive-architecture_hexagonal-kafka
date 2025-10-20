package com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String message,
        Object errors,
        String timestamp,
        String path
) {
}
