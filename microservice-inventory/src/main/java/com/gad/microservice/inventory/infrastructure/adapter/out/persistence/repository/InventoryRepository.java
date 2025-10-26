package com.gad.microservice.inventory.infrastructure.adapter.out.persistence.repository;

import com.gad.microservice.inventory.infrastructure.adapter.out.persistence.entity.InventoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface InventoryRepository extends R2dbcRepository<InventoryEntity, Long> {
}
