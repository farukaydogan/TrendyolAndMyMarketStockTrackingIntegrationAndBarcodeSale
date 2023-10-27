package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @Hidden
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    private String status;
    private Double totalPrice;
    private Date orderDate;

    @PrePersist
    public void prePersist() {
        this.orderDate = new Date(); // Bu metot, entity kaydedilirken otomatik olarak tarih oluşturacaktır.
    }

}
