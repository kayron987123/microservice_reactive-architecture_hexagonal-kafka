package com.gad.microservice.order.application.port.in;

import com.gad.microservice.order.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetOrderUseCase {
    Flux<Order> getAllOrders();
    Mono<Order> getOrderById(Long idOrder);
}
