package com.gad.microservice.order.application.port.in;

import com.gad.microservice.order.domain.Order;
import reactor.core.publisher.Mono;

public interface CreateOrderUseCase {
    Mono<Order> createOrder(Order order);
}
