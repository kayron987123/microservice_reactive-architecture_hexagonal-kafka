package com.gad.microservice.inventory.infrastructure.adapter.out.persistence;

import com.gad.microservice.inventory.application.port.out.InventoryPersistencePort;
import com.gad.microservice.inventory.domain.exception.InventoryNotFoundException;
import com.gad.microservice.inventory.domain.model.Inventory;
import com.gad.microservice.inventory.infrastructure.adapter.out.persistence.mapper.InventoryMapper;
import com.gad.microservice.inventory.infrastructure.adapter.out.persistence.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryPersistenceAdapter implements InventoryPersistencePort {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper mapper;

    @Override
    public Mono<Inventory> save(Inventory inventory) {
        return Mono.just(inventory)
                .transform(mapper::toMonoEntity)
                .flatMap(inventoryRepository::save)
                .transform(mapper::toMonoModel)
                .doOnSuccess(inventorySaved -> log.info("Inventory saved with id: {}", inventorySaved.getId()))
                .doOnError(error -> log.error("Error saving inventory", error));
    }

    @Override
    public Mono<Inventory> findById(Long id) {
        return inventoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new InventoryNotFoundException("Inventory not found with id: " + id)))
                .transform(mapper::toMonoModel)
                .doOnSuccess(i -> log.info("Found inventory with id: {}", id))
                .doOnError(error -> log.error("Error finding inventory with id: {}",id, error));
    }

    @Override
    public Flux<Inventory> findAll() {
        return inventoryRepository.findAll()
                .transform(mapper::toFluxModel)
                .doOnNext(inventory -> log.info("Found inventory with id: {}", inventory.getId()))
                .doOnError(error -> log.error("Error finding inventory", error));
    }
}
