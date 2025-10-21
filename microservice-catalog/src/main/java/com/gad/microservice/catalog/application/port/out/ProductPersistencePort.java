package com.gad.microservice.catalog.application.port.out;

import com.gad.microservice.catalog.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
    Mono<Product> findById(Long id);
    Flux<Product> findAll(Integer page, Integer size, String sortField, String sortDirection);
    Mono<Long> countAll();
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Long id);
    Flux<Product> findProductsByNameOrCategory(String name, String category);
    Mono<Product> update(Long id, Product product);
}
