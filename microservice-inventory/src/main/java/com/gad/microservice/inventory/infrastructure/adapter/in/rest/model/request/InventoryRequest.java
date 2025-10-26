package com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.request;

public record InventoryRequest(
        Long productId,
        Integer quantity
) {
}
