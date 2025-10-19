package com.gad.microservice.order.application.port.in;

import reactor.core.publisher.Mono;

public interface RemoveItemFromOrderUseCase {
    Mono<Void> removeItemFromOrder(Long orderId, Long itemId);
}
