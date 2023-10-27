package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    //    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;
    private String barcode;
//    private String stockCode;
    private String name;
    private String colorCode;
    private Double price;
    private Double trendyolPrice;
    private Integer stockQuantity;
    private Integer lotNumber;
    private Double unitWeight; // Gram cinsinden birim ağırlık


    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Package aPackage;


//    @ElementCollection(fetch =FetchType.EAGER)
    private String imagePath;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<TrendyolProducts> trendyolProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @Hidden
    @JsonIgnore
    private User user;



}
