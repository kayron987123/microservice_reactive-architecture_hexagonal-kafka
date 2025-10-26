package com.gad.microservice.inventory.infrastructure.adapter.in.rest;

import com.gad.microservice.inventory.application.port.in.CreateStockUseCase;
import com.gad.microservice.inventory.application.port.in.GetInventoryUseCase;
import com.gad.microservice.inventory.application.port.in.UpdateStockUseCase;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.mapper.InventoryRestMapper;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.request.InventoryRequest;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.request.UpdateStockRequest;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response.DataResponse;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response.InventoryDto;
import com.gad.microservice.inventory.utils.MethodsUtils;
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
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryRestAdapter {
    private final CreateStockUseCase createStockUseCase;
    private final GetInventoryUseCase getInventoryUseCase;
    private final UpdateStockUseCase updateStockUseCase;
    private final InventoryRestMapper inventoryRestMapper;

    @GetMapping
    public Mono<ResponseEntity<DataResponse<List<InventoryDto>>>> getInventory() {
        return getInventoryUseCase.getAllInventories()
                .transform(inventoryRestMapper::toResponseFlux)
                .collectList()
                .map(inventoryResponses -> ResponseEntity.ok(
                        DataResponse.<List<InventoryDto>>builder()
                                .status(HttpStatus.OK.value())
                                .message("Products retrieved successfully")
                                .data(inventoryResponses)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse<InventoryDto>>> getInventoryById(@PathVariable("id") Long id) {
        return getInventoryUseCase.getInventoryByProductId(id)
                .transform(inventoryRestMapper::toResponseMono)
                .map(inventoryResponse -> ResponseEntity.ok(
                        DataResponse.<InventoryDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("Products retrieved successfully")
                                .data(inventoryResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse<InventoryDto>>> createInventory(@RequestBody InventoryRequest request,
                                                                            ServerWebExchange serverRequest) {
        return createStockUseCase.createNewStock(inventoryRestMapper.toModel(request))
                .transform(inventoryRestMapper::toResponseMono)
                .map(inventoryDto -> {
                    URI location = UriComponentsBuilder
                            .fromUri(serverRequest.getRequest().getURI())
                            .path("/{id}")
                            .buildAndExpand(inventoryDto.id())
                            .toUri();

                    return ResponseEntity.created(location)
                            .body(DataResponse.<InventoryDto>builder()
                                    .status(HttpStatus.CREATED.value())
                                    .message("Product created successfully")
                                    .data(inventoryDto)
                                    .timestamp(MethodsUtils.datetimeNowFormatted())
                                    .build());
                });

    }

    @PutMapping("/{id}/stock/increase")
    public Mono<ResponseEntity<DataResponse<InventoryDto>>> updateStockIncrement(@PathVariable("id") Long id,
                                                                                 @RequestBody UpdateStockRequest request) {
        return updateStockUseCase.updateIncreaseStockByProductId(id, request.quantity())
                .transform(inventoryRestMapper::toResponseMono)
                .map(inventoryResponse -> ResponseEntity.ok(
                        DataResponse.<InventoryDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("Product updated successfully")
                                .data(inventoryResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }


    @PutMapping("/{id}/stock/decrease")
    public Mono<ResponseEntity<DataResponse<InventoryDto>>> updateStockDegressive(@PathVariable("id") Long id,
                                                                                  @RequestBody UpdateStockRequest request) {
        return updateStockUseCase.updateDecreaseStockByProductId(id, request.quantity())
                .transform(inventoryRestMapper::toResponseMono)
                .map(inventoryResponse -> ResponseEntity.ok(
                        DataResponse.<InventoryDto>builder()
                                .status(HttpStatus.OK.value())
                                .message("Product updated successfully")
                                .data(inventoryResponse)
                                .timestamp(MethodsUtils.datetimeNowFormatted())
                                .build())
                );
    }
}
