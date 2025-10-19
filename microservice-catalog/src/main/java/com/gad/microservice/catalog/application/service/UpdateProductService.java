package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.UpdateProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.exception.ProductNotFoundException;
import com.gad.microservice.catalog.domain.model.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Mono<Product> updateProductById(Long id, Product product) {
        return persistencePort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("The product was not found with the id: " + id)))
                .flatMap(productExisting -> {
                    productExisting.setName(product.getName());
                    productExisting.setDescription(product.getDescription());
                    productExisting.setPrice(product.getPrice());
                    productExisting.setCategory(product.getCategory());
                    productExisting.setUpdatedAt(LocalDateTime.now());
                    return persistencePort.save(productExisting);
                });
    }
}
