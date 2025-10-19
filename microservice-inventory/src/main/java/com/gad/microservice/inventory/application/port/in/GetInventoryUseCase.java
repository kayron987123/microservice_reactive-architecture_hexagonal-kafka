package com.gad.microservice.inventory.application.port.in;

import com.gad.microservice.inventory.domain.Inventory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GetInventoryUseCase {
    Flux<Inventory> getAllInventories();
    Mono<Inventory> getInventoryByProductId(Long productId);
}
