package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Ek sorgu metodları gerektiğinde buraya eklenebilir.
}
