package com.gad.microservice.payment.application.port.in;

import com.gad.microservice.payment.domain.Payment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPaymentUseCase {
    Flux<Payment> getAllPayments();
    Mono<Payment> getPaymentByOrderId(Long orderId);
}
