package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.UpdateProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Mono<Product> updateProductById(Long id, Product product) {
        return persistencePort.update(id, product)
                .doOnSuccess(p -> log.info("Product updated with id: {}", id))
                .doOnError(error -> log.error("Error updating product with id: {}", id, error));
    }
}
