package com.gad.microservice.inventory.application.port.service;

import com.gad.microservice.inventory.application.port.in.GetInventoryUseCase;
import com.gad.microservice.inventory.application.port.out.InventoryPersistencePort;
import com.gad.microservice.inventory.domain.exception.InventoryNotFoundException;
import com.gad.microservice.inventory.domain.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetInventoryService implements GetInventoryUseCase {
    private final InventoryPersistencePort persistencePort;

    @Override
    public Flux<Inventory> getAllInventories() {
        return persistencePort.findAll()
                .doOnComplete(() -> log.info("Completed reading all inventories"))
                .doOnError(error -> log.error("Error fetching inventory", error));
    }

    @Override
    public Mono<Inventory> getInventoryByProductId(Long productId) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new InventoryNotFoundException("The inventory was not found with the id:" + productId)))
                .doOnSuccess(i -> log.info("Found inventory with id: {}", i.getId()))
                .doOnError(error -> log.error("Error fetching inventory", error));
    }
}
