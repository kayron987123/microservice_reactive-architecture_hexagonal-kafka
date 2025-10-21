package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.DeleteProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteProductService implements DeleteProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Mono<Void> deleteProductById(Long id) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("The product was not found with the id: " + id)))
                .flatMap(productExisting -> persistencePort.deleteById(productExisting.getId()))
                .doOnSuccess(deletedProduct -> log.info("Product deleted with id: {}", id))
                .doOnError(error -> log.error("Error deleting product with id: {}", id, error));
    }
}
