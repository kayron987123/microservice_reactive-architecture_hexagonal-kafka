package com.gad.microservice.catalog.infrastructure.adapter.in.rest;

import com.gad.microservice.catalog.application.port.in.CreateProductUseCase;
import com.gad.microservice.catalog.application.port.in.DeleteProductUseCase;
import com.gad.microservice.catalog.application.port.in.GetProductUseCase;
import com.gad.microservice.catalog.application.port.in.UpdateProductUseCase;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.mapper.ProductRestMapper;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.dto.ProductDto;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.request.ProductRequest;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.DataResponse;
import com.gad.microservice.catalog.infrastructure.adapter.in.rest.model.response.PagedResponse;
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
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ProductRestMapper mapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse<ProductDto>>> findProductById(@PathVariable("id") Long id) {
        return getProductUseCase.getProductById(id)
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
        return getProductUseCase.getAllProducts(page, size, field, sort)
                .map(pagedProducts -> {
                    List<ProductDto> productDtos = pagedProducts.data().stream()
                            .map(mapper::toResponse)
                            .toList();

                    PagedResponse<ProductDto> pagedResponseDto = PagedResponse.<ProductDto>builder()
                            .page(pagedProducts.page())
                            .size(pagedProducts.size())
                            .totalElements(pagedProducts.totalElements())
                            .data(productDtos)
                            .build();

                    return ResponseEntity.ok(
                            DataResponse.<PagedResponse<ProductDto>>builder()
                                    .status(HttpStatus.OK.value())
                                    .message("Products retrieved successfully")
                                    .data(pagedResponseDto)
                                    .timestamp(MethodsUtils.datetimeNowFormatted())
                                    .build()
                    );
                });
    }

    @GetMapping("/filter")
    public Mono<ResponseEntity<DataResponse<List<ProductDto>>>> findProductsByNameOrCategory(@RequestParam(value = "name") String name,
                                                                                             @RequestParam(value = "category") String category) {
        return getProductUseCase.getProductsByNameOrCategory(name, category)
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
        return createProductUseCase.createProduct(mapper.toModel(request))
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

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse<ProductDto>>> updateProduct(@PathVariable("id") Long id,
                                                                        @RequestBody @Valid ProductRequest request,
                                                                        ServerWebExchange serverRequest) {
        return updateProductUseCase.updateProductById(id, mapper.toModel(request))
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
                                    .message("Product updated successfully")
                                    .data(productDto)
                                    .timestamp(MethodsUtils.datetimeNowFormatted())
                                    .build());
                });

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return deleteProductUseCase.deleteProductById(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }
}
