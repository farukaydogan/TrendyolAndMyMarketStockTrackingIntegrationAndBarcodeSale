package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.Order;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByBarcode(String barcode);

}
