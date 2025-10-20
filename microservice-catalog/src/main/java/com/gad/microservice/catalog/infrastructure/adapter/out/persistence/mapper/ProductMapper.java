package com.gad.microservice.catalog.infrastructure.adapter.out.persistence.mapper;

import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   Product toModel(ProductEntity productEntity);

   default Mono<Product> toProductMono(Mono<ProductEntity> product) {
       return product.map(this::toModel);
   }

   default Flux<Product> toProductFlux(Flux<ProductEntity> products) {
       return products.map(this::toModel);
   }

   ProductEntity toEntity(Product product);

   default Mono<ProductEntity> toEntityMono(Mono<Product> product) {
       return product.map(this::toEntity);
   }
}

