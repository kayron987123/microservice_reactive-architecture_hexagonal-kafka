package com.gad.microservice.inventory.infrastructure.adapter.out.persistence.mapper;

import com.gad.microservice.inventory.domain.model.Inventory;
import com.gad.microservice.inventory.infrastructure.adapter.out.persistence.entity.InventoryEntity;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toModel(InventoryEntity entity);

    default Mono<Inventory> toMonoModel(Mono<InventoryEntity> entity) {
        return entity.map(this::toModel);
    }

    default Flux<Inventory> toFluxModel(Flux<InventoryEntity> entity) {
        return entity.map(this::toModel);
    }

    InventoryEntity toEntity(Inventory model);

    default Mono<InventoryEntity> toMonoEntity(Mono<Inventory> model) {
        return model.map(this::toEntity);
    }
}
