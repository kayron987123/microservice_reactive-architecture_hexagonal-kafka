package com.gad.microservice.payment.application.port.in;

import com.gad.microservice.payment.domain.Payment;
import reactor.core.publisher.Mono;

public interface ProcessPaymentUseCase {
    Mono<Payment> processPayment(Payment payment);
}
