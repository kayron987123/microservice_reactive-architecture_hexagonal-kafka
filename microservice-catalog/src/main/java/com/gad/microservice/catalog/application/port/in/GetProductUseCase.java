package com.gad.microservice.catalog.application.port.in;

import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.PagedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GetProductUseCase {
    Mono<PagedResponse<Product>> getAllProducts(Integer page, Integer size, String sortField, String sortDirection);
    Mono<Product> getProductById(Long id);
    Flux<Product> getProductsByNameOrCategory(String name, String category);
}
