package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

@Entity
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer packageId;
    private String type; // Ambalaj türü

    private Integer amount;
    private String status; // Ambalaj durumu: boş, dolu, kısmen dolu
    // Diğer alanlar ve ilişkiler
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
//    @Hidden
//    @JsonIgnore
    private Product product;

}