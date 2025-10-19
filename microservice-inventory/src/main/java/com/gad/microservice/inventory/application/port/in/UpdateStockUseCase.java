package com.gad.microservice.inventory.application.port.in;

import com.gad.microservice.inventory.domain.Inventory;
import reactor.core.publisher.Mono;

public interface UpdateStockUseCase {
    Mono<Inventory> updateIncreaseStockByProductId(Long productId, Integer newStock);
    Mono<Inventory> updateDecreaseStockByProductId(Long productId, Integer quantity);
}
