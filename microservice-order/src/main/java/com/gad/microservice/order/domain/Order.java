package com.gad.microservice.order.domain;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Integer id;
    private Long customerId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItem> items;
}
