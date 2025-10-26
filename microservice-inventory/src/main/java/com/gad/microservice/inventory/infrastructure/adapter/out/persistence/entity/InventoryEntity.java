package com.gad.microservice.inventory.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("inventories")
public class InventoryEntity {
    @Id
    private Long id;

    @Column(value = "product_id")
    private Long productId;
    private Integer quantity;
    @Column(value = "last_updated")
    private LocalDateTime lastUpdated;
}
