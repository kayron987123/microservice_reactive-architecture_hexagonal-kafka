package com.gad.microservice.inventory.application.port.service;

import com.gad.microservice.inventory.application.port.in.UpdateStockUseCase;
import com.gad.microservice.inventory.application.port.out.InventoryPersistencePort;
import com.gad.microservice.inventory.domain.exception.InventoryNotFoundException;
import com.gad.microservice.inventory.domain.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateStockService implements UpdateStockUseCase {
    private final InventoryPersistencePort persistencePort;

    @Override
    public Mono<Inventory> updateIncreaseStockByProductId(Long productId, Integer newStock) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new InventoryNotFoundException("The inventory was not found with the id:" + productId)))
                .flatMap(inventory -> {
                    inventory.addStock(newStock);
                    return persistencePort.save(inventory);
                })
                .doOnSuccess(inventorySaved -> log.info("Inventory updated successfully with id: {}", productId))
                .doOnError(error -> log.error("Error updating inventory with id: {}", productId, error));
    }

    @Override
    public Mono<Inventory> updateDecreaseStockByProductId(Long productId, Integer quantity) {
        return persistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new InventoryNotFoundException("The inventory was not found with the id:" + productId)))
                .flatMap(inventory -> {
                    inventory.removeStock(quantity);
                    return persistencePort.save(inventory);
                })
                .doOnSuccess(inventorySaved -> log.info("Inventory updated successfully with id: {}", productId))
                .doOnError(error -> log.error("Error updating inventory with id: {}", productId, error));
    }
}
