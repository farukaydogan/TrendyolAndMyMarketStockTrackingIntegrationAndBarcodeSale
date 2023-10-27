package com.fta.stock.tracking.model;

import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private Integer quantity;
    // Getter ve Setter metodları...
}