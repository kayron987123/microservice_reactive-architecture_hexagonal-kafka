package com.gad.microservice.catalog.infrastructure.adapter.in.rest.mapper;

import com.gad.microservice.catalog.domain.model.Product;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.dto.ProductDto;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductRestMapper {


    ProductDto toResponse(Product product);

    default Mono<ProductDto> toMonoDTO(Mono<Product> product) {
        return product.map(this::toResponse);
    }

    default Flux<ProductDto> toFluxDTO(Flux<Product> product) {
        return product.map(this::toResponse);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toModel(ProductRequest request);
}
