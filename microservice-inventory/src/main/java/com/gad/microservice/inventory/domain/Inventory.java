package com.gad.microservice.inventory.domain;

import java.time.LocalDateTime;

public class Inventory {
    private Long id;
    private Long productId;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}
