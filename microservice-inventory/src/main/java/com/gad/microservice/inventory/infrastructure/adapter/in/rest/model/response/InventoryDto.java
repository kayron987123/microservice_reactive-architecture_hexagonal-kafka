package com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response;


public record InventoryDto(
        Long id,
        Long productId,
        Integer quantity
) {
}
