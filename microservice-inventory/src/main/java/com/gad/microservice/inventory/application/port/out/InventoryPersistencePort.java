package com.gad.microservice.inventory.application.port.out;

import com.gad.microservice.inventory.domain.model.Inventory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryPersistencePort {
    Mono<Inventory> save(Inventory inventory);
    Mono<Inventory> findById(Long id);
    Flux<Inventory> findAll();
}
