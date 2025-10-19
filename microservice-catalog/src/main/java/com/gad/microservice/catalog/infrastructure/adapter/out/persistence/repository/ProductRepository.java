package com.gad.microservice.catalog.infrastructure.adapter.out.persistence.repository;

import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends R2dbcRepository<ProductEntity, Long> {
    Flux<ProductEntity> findProductEntitiesByNameOrCategory(String name, String category);
}
