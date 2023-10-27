package com.fta.stock.tracking.repository;


import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.TrendyolProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrendyolProductRepository extends JpaRepository<TrendyolProducts, Integer> {
    Optional<TrendyolProducts> findByBarcode(String barcode);

}

