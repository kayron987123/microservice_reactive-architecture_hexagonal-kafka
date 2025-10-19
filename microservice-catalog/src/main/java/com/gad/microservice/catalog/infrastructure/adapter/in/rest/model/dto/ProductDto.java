package com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String description,
        String category,
        BigDecimal price
) {
}
