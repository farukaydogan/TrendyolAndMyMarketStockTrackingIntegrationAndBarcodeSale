package com.fta.stock.tracking.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "update_products")
public class UpdateProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long updateProductId;

    private String productName;
    private Double newPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @PrePersist
    public void prePersist() {
        this.updateTime = new Date(); // Bu metot, entity kaydedilirken otomatik olarak tarih oluşturacaktır.
    }
}