package com.gad.microservice.catalog.application.service;

import com.gad.microservice.catalog.application.port.in.GetProductUseCase;
import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.exception.ProductNotFoundException;
import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {
    private final ProductPersistencePort persistencePort;

    @Override
    public Mono<PagedResponse<Product>> getAllProducts(Integer page, Integer size, String sortField, String sortDirection) {
        Flux<Product> productsFlux = persistencePort.findAll(page, size, sortField, sortDirection);
        Mono<Long> totalCountMono = persistencePort.countAll();

        return Mono.zip(productsFlux.collectList(), totalCountMono)
                .map(tuple -> PagedResponse.<Product>builder()
                        .page(page)
                        .size(size)
                        .totalElements(tuple.getT2())
                        .data(tuple.getT1())
                        .build())
                .doOnSuccess(pagedResponse -> log.info("PagedResponse: {}", pagedResponse))
                .doOnError(error -> log.error(error.getMessage(), error));
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
