package com.gad.microservice.order.application.port.in;

import com.gad.microservice.order.domain.Order;
import com.gad.microservice.order.domain.OrderItem;
import reactor.core.publisher.Mono;

public interface AddItemToOrderUseCase {
    Mono<Order> addItemToOrder(Long orderId, OrderItem item);
}
