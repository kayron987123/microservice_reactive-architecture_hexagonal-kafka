package com.gad.microservice.catalog.application.port.in;

import reactor.core.publisher.Mono;

public interface DeleteProductUseCase {
    Mono<Void> deleteProductById(Long id);
}
