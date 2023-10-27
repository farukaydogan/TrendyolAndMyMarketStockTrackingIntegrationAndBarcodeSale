package com.fta.stock.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
public class TrendyolOrder {

    @Id
    private String orderNumber;


    @ManyToOne
    @Hidden
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "trendyolOrder", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    private String status;
    private Double totalPrice;
    private Date orderDate;
}
