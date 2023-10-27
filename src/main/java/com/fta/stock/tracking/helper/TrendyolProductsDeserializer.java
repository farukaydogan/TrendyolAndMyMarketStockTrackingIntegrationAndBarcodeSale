package com.fta.stock.tracking.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.TrendyolProducts;
import com.fta.stock.tracking.service.ProductService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Component;

import java.io.IOException;
@JsonComponent
public class TrendyolProductsDeserializer extends JsonDeserializer<TrendyolProducts> {
    private  final ProductService productService;
    @Autowired
    public TrendyolProductsDeserializer(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public TrendyolProducts deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        TrendyolProducts product = new TrendyolProducts();
        // ... diğer alanları doldurun, örneğin:
        // product.setName(node.get("name").asText());

        if (node.has("images") && node.get("images").isArray() && !node.get("images").isEmpty()) {
            product.setImages(node.get("images").get(0).get("url").asText());
        }
        product.setBarcode(node.get("barcode").asText());
        product.setStockCode(node.get("stockCode").asText());
        product.setSalePrice(node.get("salePrice").asDouble());
        product.setProduct(productService.findByBarcode(product.getBarcode()).orElseThrow());

        return product;
    }
}
