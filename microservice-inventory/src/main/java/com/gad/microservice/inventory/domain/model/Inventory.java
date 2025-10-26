package com.gad.microservice.inventory.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    private Long id;
    private Long productId;
    private Integer quantity;
    private LocalDateTime lastUpdated;

    public void addStock(Integer quantity){
        if (quantity < 0){
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.quantity += quantity;
    }

    public void removeStock(Integer quantity){
        if (quantity < 0){
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        if (quantity > this.quantity){
            throw new IllegalArgumentException("Stock quantity cannot be greater than the quantity");
        }

        this.quantity -= quantity;
    }
}
