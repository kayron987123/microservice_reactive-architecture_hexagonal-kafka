package com.gad.microservice.catalog.application.port.out;

import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.PagedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
    Mono<Product> findById(Long id);
    Mono<PagedResponse<Product>> findAll(Integer page, Integer size, String sortField, String sortDirection);
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Long id);
    Flux<Product> findProductsByNameOrCategory(String name, String category);
}
