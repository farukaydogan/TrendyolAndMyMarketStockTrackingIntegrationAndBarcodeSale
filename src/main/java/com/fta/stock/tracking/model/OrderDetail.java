package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JsonIgnore
    @Hidden
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne
    @JsonIgnore
    @Hidden
    @JoinColumn(name = "trendyolOrderId")
    private TrendyolOrder trendyolOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Hidden
    @JoinColumn(name = "productId")
    private Product product;

    private Integer quantity;
    private Double price;
}