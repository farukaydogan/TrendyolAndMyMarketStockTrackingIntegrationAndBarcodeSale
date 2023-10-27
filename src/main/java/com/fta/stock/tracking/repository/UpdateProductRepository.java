package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.UpdateProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateProductRepository extends JpaRepository<UpdateProduct, Long> {
    // Custom query methods if needed
}
