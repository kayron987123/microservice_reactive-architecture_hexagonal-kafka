package com.gad.microservice.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Long orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private String status;
    private LocalDateTime transactionDate;
}
