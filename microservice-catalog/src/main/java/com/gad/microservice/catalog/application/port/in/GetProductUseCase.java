package com.gad.microservice.catalog.application.port.in;

import com.gad.microservice.catalog.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GetProductUseCase {
    Flux<Product> getAllProducts(Integer page, Integer size);
    Mono<Product> getProductById(Long id);
    Flux<Product> getProductsByNameOrCategory(String name, String category);
}
