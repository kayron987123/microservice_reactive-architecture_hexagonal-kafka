package com.gad.microservice.catalog.infrastructure.adapter.in.rest;

import com.gad.microservice.catalog.infrastructure.adapter.in.rest.mapper.ProductRestMapper;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.request.ProductRequest;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.DataResponse;
import com.gad.microservice.catalog.infrastructure.adapter.out.persistence.ProductPersistenceAdapter;
import com.gad.microservice.catalog.utils.MethodsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductRestAdapter {
    private final ProductPersistenceAdapter persistenceAdapter;
    private final ProductRestMapper mapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> findProductById(@PathVariable Long id) {
        return persistenceAdapter.findById(id)
                .transform(mapper::toMonoDTO)
                .map(productResponse -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Product retrieved successfully")
                                .data(productResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return persistenceAdapter.findAll(page, size)
                .transform(mapper::toFluxDTO)
                .collectList()
                .map(productResponse -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Products retrieved successfully")
                                .data(productResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/filter")
    public Mono<ResponseEntity<DataResponse>> findProductsByNameOrCategory(@RequestParam(value = "name") String name,
                                                                           @RequestParam(value = "category") String category) {
        return persistenceAdapter.findProductsByNameOrCategory(name, category)
                .transform(mapper::toFluxDTO)
                .collectList()
                .map(productResponse -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Products retrieved successfully")
                                .data(productResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> addProduct(@RequestBody ProductRequest request,
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
                            .body(DataResponse.builder()
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
