package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.GetProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.exception.ProductNotFoundException;
import com.gad.microservice.catalog.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Flux<Product> getAllProducts(Integer page, Integer size) {
        return persistencePort.findAll(page, size)
                .doOnComplete(() -> log.info("Completed reading all products"))
                .doOnError(error -> log.error("Error reading all products", error));
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("The product was not found with the id: " + id)))
                .doOnSuccess(product -> log.info("Product found with id: {}", id))
                .doOnError(error -> log.error("Error searching product with id: {}", id, error));
    }

    @Override
    public Flux<Product> getProductsByNameOrCategory(String name, String category) {
        return persistencePort.findProductsByNameOrCategory(name, category)
                .doOnComplete(() -> log.info("Completed reading all products by name or category"))
                .doOnError(error -> log.error("Error fetching products by name: {} or category: {}", name, category, error));
    }
}
