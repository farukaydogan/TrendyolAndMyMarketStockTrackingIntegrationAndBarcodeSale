package com.fta.stock.tracking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.TrendyolOrder;
import com.fta.stock.tracking.model.TrendyolProducts;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.repository.ProductRepository;
import com.fta.stock.tracking.repository.TrendyolProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrendyolProductService {
    private final TrendyolProductRepository trendyolProductRepository;

    private final ProductService productService;


    public List<TrendyolProducts> parseTrendyolProductResponse(String jsonResponse) throws IOException, JsonProcessingException {
        List<TrendyolProducts> trendyolProductsList = new ArrayList<>();
        JsonNode contentNode = new ObjectMapper().readTree(jsonResponse).get("content");

        if (contentNode.isArray()) {
            for (JsonNode productNode : contentNode) {
                TrendyolProducts trendyolProduct = new TrendyolProducts();

                // JSON verisinden TrendyolProducts nesnesine bilgileri ata
                if (productNode.has("images") && productNode.get("images").isArray() && !productNode.get("images").isEmpty()) {
                    trendyolProduct.setImages(productNode.get("images").get(0).get("url").asText());
                }
                trendyolProduct.setBarcode(productNode.get("barcode").asText());
                trendyolProduct.setStockCode(productNode.get("stockCode").asText());
                trendyolProduct.setSalePrice(productNode.get("salePrice").asDouble());

                // Barcode kullanarak ProductService ile Product nesnesini bul ve ata
                Product product = productService.findByBarcode(trendyolProduct.getBarcode())
                        .orElseThrow(() -> new RuntimeException("Product not found for barcode: " + trendyolProduct.getBarcode()));
                trendyolProduct.setProduct(product);

                trendyolProductsList.add(trendyolProduct);
            }
        }
        return trendyolProductsList;
    }

    public void flushTableAndSaveProduct(List<TrendyolProducts> trendyolProductList, User user) {
        for (TrendyolProducts trendyolProduct : trendyolProductList) {
            Optional<Product> originalProductOpt = productService.findByBarcode(trendyolProduct.getStockCode());
            Optional<TrendyolProducts> existingProduct = trendyolProductRepository.findByBarcode(trendyolProduct.getBarcode());
            if (existingProduct.isPresent()) {
                TrendyolProducts dbProduct = existingProduct.get();
                dbProduct.setSalePrice(trendyolProduct.getSalePrice());
                dbProduct.setUser(user);
                if (dbProduct.getImages() == null && trendyolProduct.getImages() != null && !trendyolProduct.getImages().isEmpty()) {
                 dbProduct.setImages(trendyolProduct.getImages());
                }
                trendyolProductRepository.save(dbProduct);
            } else {
                if (originalProductOpt.isPresent()) {
                    Product originalProduct = originalProductOpt.get();
                    trendyolProduct.setProduct(originalProduct);
                    trendyolProduct.setUser(user);
                    trendyolProductRepository.save(trendyolProduct);

                    // Eğer Product'ta resim yoksa ve TrendyolProducts'tan gelen resimler varsa, Product'ı güncelle
                    if (originalProduct.getImagePath() == null && trendyolProduct.getImages() != null && !trendyolProduct.getImages().isEmpty()) {
                        productService.updateImage(originalProduct, trendyolProduct.getImages()); // İlk resmi alarak güncelliyoruz
                    }
                }
            }
            //         stoklari guncelle ancak burada bir sikinti var ayni urunden birden fazla var
            //         o yuzden  orderlari bir araya getirip butun stogu senkronize etmelyizi
//            productService.updateStockQuantity(originalProductOpt.orElseThrow(),trendyolProduct.getStockQuantity());
        }
    }


    public TrendyolProducts saveTrendyolProducts(TrendyolProducts trendyolProducts) {

        return trendyolProductRepository.save(trendyolProducts);
    }



}
