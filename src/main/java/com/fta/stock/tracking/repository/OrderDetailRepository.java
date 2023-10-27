package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Custom query methods if needed
}
