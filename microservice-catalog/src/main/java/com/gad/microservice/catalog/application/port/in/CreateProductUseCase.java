package com.gad.microservice.catalog.application.port.in;

import com.gad.microservice.catalog.domain.model.Product;
import reactor.core.publisher.Mono;

public interface CreateProductUseCase {
    Mono<Product> createProduct(Product product);
}
