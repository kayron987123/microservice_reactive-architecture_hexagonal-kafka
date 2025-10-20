package com.gad.microservice.catalog.infrastructure.adapter.in.rest;

import com.gad.microservice.catalog.infrastructure.adapter.in.rest.mapper.ProductRestMapper;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.dto.ProductDto;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.request.ProductRequest;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.DataResponse;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.PagedResponse;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.ProductPersistenceAdapter;
import com.gad.microservice.catalog.utils.MethodsUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductRestAdapter {
    private final ProductPersistenceAdapter persistenceAdapter;
    private final ProductRestMapper mapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse<ProductDto>>> findProductById(@PathVariable("id") Long id) {
        return persistenceAdapter.findById(id)
                .transform(mapper::toMonoDTO)
                .map(productResponse -> ResponseEntity.ok(
                        DataResponse.<ProductDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("Product retrieved successfully")
                                .data(productResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<DataResponse<PagedResponse<ProductDto>>>> findAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                         @RequestParam(value = "field", defaultValue = "name") String field,
                                                                                         @RequestParam(value = "sort", defaultValue = "asc") String sort) {
        return persistenceAdapter.findAll(page, size, field, sort)
                .map(pagedProducts -> {
                    var productDtos = pagedProducts.data().stream()
                            .map(mapper::toResponse)
                            .toList();

                    var pagedResponse = PagedResponse.<ProductDto>builder()
                            .page(pagedProducts.page())
                            .size(pagedProducts.size())
                            .totalElements(pagedProducts.totalElements())
                            .data(productDtos)
                            .build();

                    var response = DataResponse.<PagedResponse<ProductDto>>builder()
                            .status(HttpStatus.OK.value())
                            .message("Products retrieved successfully")
                            .data(pagedResponse)
                            .timestamp(MethodsUtils.datetimeNowFormatted())
                            .build();

                    return ResponseEntity.ok(response);
                });
    }

    @GetMapping("/filter")
    public Mono<ResponseEntity<DataResponse<List<ProductDto>>>> findProductsByNameOrCategory(@RequestParam(value = "name") String name,
                                                                                             @RequestParam(value = "category") String category) {
        return persistenceAdapter.findProductsByNameOrCategory(name, category)
                .transform(mapper::toFluxDTO)
                .collectList()
                .map(productResponses -> ResponseEntity.ok(
                        DataResponse.<List<ProductDto>>builder()
                                .status(HttpStatus.OK.value())
                                .message("Products retrieved successfully")
                                .data(productResponses)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse<ProductDto>>> addProduct(@RequestBody @Valid ProductRequest request,
                                                                     ServerWebExchange serverRequest) {
        return persistenceAdapter.save(mapper.toModel(request))
                .map(mapper::toResponse)
                .map(productDto -> {
                    URI location = UriComponentsBuilder
                            .fromUri(serverRequest.getRequest().getURI())
                            .path("/{id}")
                            .buildAndExpand(productDto.id())
                            .toUri();

                    return ResponseEntity.created(location)
                            .body(DataResponse.<ProductDto>builder()
                                    .status(HttpStatus.CREATED.value())
                                    .message("Product created successfully")
                                    .data(productDto)
                                    .timestamp(MethodsUtils.datetimeNowFormatted())
                                    .build());
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return persistenceAdapter.deleteById(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }
}
