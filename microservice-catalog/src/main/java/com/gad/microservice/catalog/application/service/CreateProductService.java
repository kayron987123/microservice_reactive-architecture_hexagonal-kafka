package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.CreateProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Mono<Product> createProduct(Product product) {
        return persistencePort.save(product)
                .doOnSuccess(p -> log.info("Product created with id: {}", p.getId()))
                .doOnError(error -> log.error("Error saving product", error));
    }
}
