package com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response;

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
