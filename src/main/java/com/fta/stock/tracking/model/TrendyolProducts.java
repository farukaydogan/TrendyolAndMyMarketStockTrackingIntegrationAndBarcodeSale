package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fta.stock.tracking.helper.TrendyolProductsDeserializer;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = TrendyolProductsDeserializer.class)
@Data
@Entity
public class TrendyolProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer trendyolProductId;

    private String  barcode;
    private Double  salePrice;

    private String stockCode;

    private Integer stockQuantity;
    private String images;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @Hidden
    @JsonIgnore
    private Product product;


    @ManyToOne
    @JoinColumn(name = "userId")
    @Hidden
    @JsonIgnore
    private User user;



}
