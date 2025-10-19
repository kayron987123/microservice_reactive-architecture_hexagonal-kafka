package com.gad.microservice.order.application.port.in;

import reactor.core.publisher.Mono;

public interface CancelOrderUseCase {
    Mono<Void> cancelOrderById(Long orderId);
}
