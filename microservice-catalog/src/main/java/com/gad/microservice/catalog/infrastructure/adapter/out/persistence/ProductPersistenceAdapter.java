package com.gad.microservice.catalog.infrastructure.adapter.out.persistence;

import com.gad.microservice.catalog.application.port.out.ProductPersistencePort;
import com.gad.microservice.catalog.domain.exception.ProductNotFoundException;
import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.mapper.ProductMapper;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)))
                .transform(productMapper::toProductMono)
                .doOnSuccess(product -> log.info("Product found with id: {}", id))
                .doOnError(error -> log.error("Error finding product with id: {}", id, error));
    }

    @Override
    public Flux<Product> findAll(Integer page, Integer size, String sortField, String sortDirection) {
        Comparator<ProductEntity> comparator = getComparator(sortField, sortDirection);

        return productRepository.findAll()
                .sort(comparator)
                .skip((long) page * size)
                .take(size)
                .transform(productMapper::toProductFlux);
    }

    @Override
    public Mono<Long> countAll() {
        return productRepository.count();
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .transform(productMapper::toEntityMono)
                .flatMap(productRepository::save)
                .transform(productMapper::toProductMono)
                .doOnSuccess(productSaved -> log.info("Product saved with id: {}", productSaved.getId()))
                .doOnError(error -> log.error("Error saving product with id: {}", product.getId(), error));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)))
                .flatMap(productRepository::delete)
                .doOnError(error -> log.error("Error deleting product with id: {}", id, error));
    }

    @Override
    public Flux<Product> findProductsByNameOrCategory(String name, String category) {
        return productRepository.findProductEntitiesByNameOrCategory(name, category)
                .transform(productMapper::toProductFlux)
                .doOnNext(product -> log.info("Product found with name: {} and category: {}", product.getName(), category))
                .doOnError(error -> log.error("Error when finding products by name: {} and category: {}", name, category, error));
    }

    @Override
    public Mono<Product> update(Long id, Product product) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)))
                .flatMap(productExisting -> {
                    productExisting.setName(product.getName());
                    productExisting.setDescription(product.getDescription());
                    productExisting.setPrice(product.getPrice());
                    productExisting.setCategory(product.getCategory());
                    productExisting.setUpdatedAt(LocalDateTime.now());
                    return productRepository.save(productExisting);
                })
                .transform(productMapper::toProductMono)
                .doOnSuccess(prod -> log.info("Product updated with id: {}", id))
                .doOnError(error -> log.error("Error updating product with id: {}", id, error));
    }

    private Comparator<ProductEntity> getComparator(String sortField, String sortDirection) {
        Comparator<ProductEntity> comparator = switch (sortField.toLowerCase()) {
            case "name" -> Comparator.comparing(ProductEntity::getName,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case "price" -> Comparator.comparing(ProductEntity::getPrice,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "category" -> Comparator.comparing(ProductEntity::getCategory,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "description" -> Comparator.comparing(ProductEntity::getDescription,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "id" -> Comparator.comparing(ProductEntity::getId);
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortField);
        };

        return switch (sortDirection.toLowerCase()) {
            case "desc", "descendente" -> comparator.reversed();
            case "asc", "ascendente", "" -> comparator;
            default -> throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
        };
    }
}
