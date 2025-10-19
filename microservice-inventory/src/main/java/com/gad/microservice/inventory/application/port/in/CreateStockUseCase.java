package com.gad.microservice.inventory.application.port.in;

import com.gad.microservice.inventory.domain.Inventory;
import reactor.core.publisher.Mono;

public interface CreateStockUseCase {
    Mono<Inventory> createNewStock(Inventory inventory);
}
