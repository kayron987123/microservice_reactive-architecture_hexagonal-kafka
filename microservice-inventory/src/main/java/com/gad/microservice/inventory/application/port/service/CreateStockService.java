package com.gad.microservice.inventory.application.port.service;

import com.gad.microservice.inventory.application.port.in.CreateStockUseCase;
import com.gad.microservice.inventory.application.port.out.InventoryPersistencePort;
import com.gad.microservice.inventory.domain.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateStockService implements CreateStockUseCase {
    private final InventoryPersistencePort persistencePort;

    @Override
    public Mono<Inventory> createNewStock(Inventory inventory) {
        return persistencePort.save(inventory)
                .doOnSuccess(i -> log.info("Inventory created with id: {}", i.getId()))
                .doOnError(error -> log.error("Error saving inventory", error));
    }
}
