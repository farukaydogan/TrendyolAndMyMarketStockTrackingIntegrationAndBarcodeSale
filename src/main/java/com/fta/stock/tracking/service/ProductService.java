package com.fta.stock.tracking.service;

import com.fta.stock.tracking.exceptions.ProductNotFoundException;
import com.fta.stock.tracking.model.CartItem;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.TrendyolProducts;
import com.fta.stock.tracking.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id.longValue());
    }

    public Product addBarcodeToProduct(Long productId, String barcodeValue) {
        // Ürünü veritabanından al
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Yeni bir Barcode oluştur
        TrendyolProducts barcode = new TrendyolProducts();
        barcode.setBarcode(barcodeValue);
        barcode.setProduct(product);

        // Ürüne bu Barcode'u ekle
        product.getTrendyolProducts().add(barcode);

        // Ürünü veritabanına kaydet (Barcode da otomatik olarak kaydedilecektir)
        return productRepository.save(product);
    }

    public Optional<Product> findByBarcode(String barcode) {
        return Optional.ofNullable(productRepository.findByBarcode(barcode));
    }

    public boolean checkQuantity(CartItem item) {
        Product product = item.getProduct();
        if (product.getStockQuantity() >= item.getQuantity()) {
            return true;
        }
        return false;
    }

    @Transactional
    public Product updateProduct(Product product) {
        if (product.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null for an update operation.");
        }

        if (!productRepository.existsById(Long.valueOf(product.getProductId()))) {
            throw new IllegalArgumentException("Product with ID " + product.getProductId() + " does not exist.");
        }

        return productRepository.save(product);
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    public void updateImage(Product product, String photoPath) {
        product.setImagePath(photoPath);
        productRepository.save(product);
    }

    public void updateStockQuantity(Product product, Integer stockQuantity) {
        product.setStockQuantity(stockQuantity);
        productRepository.save(product);

    }

    public void stockDecrease(Product product, Integer stockQuantity) {
        product.setStockQuantity(product.getStockQuantity() - stockQuantity);
        saveProduct(product);
    }

    public List<Product> convertTrendyolProductsToProducts(List<TrendyolProducts> trendyolProducts) {
        return trendyolProducts.stream()
                .map(TrendyolProducts::getProduct)
                .distinct()
                .collect(Collectors.toList());
    }
}
