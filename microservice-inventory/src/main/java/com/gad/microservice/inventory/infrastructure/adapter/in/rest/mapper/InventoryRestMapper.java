package com.gad.microservice.inventory.infrastructure.adapter.in.rest.mapper;

import com.gad.microservice.inventory.domain.model.Inventory;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.request.InventoryRequest;
import com.gad.microservice.inventory.infrastructure.adapter.in.rest.model.response.InventoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryRestMapper {
    InventoryDto toResponse(Inventory inventory);

    default Mono<InventoryDto> toResponseMono(Mono<Inventory> inventory){
        return inventory.map(this::toResponse);
    }

    default Flux<InventoryDto> toResponseFlux(Flux<Inventory> inventory){
        return inventory.map(this::toResponse);
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Inventory toModel(InventoryRequest request);
}
