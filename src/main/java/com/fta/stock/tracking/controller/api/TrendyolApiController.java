package com.fta.stock.tracking.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fta.stock.tracking.helper.RestTemplateTrendyolWithHeaders;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.TrendyolOrder;
import com.fta.stock.tracking.model.TrendyolProducts;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trendyol")
public class TrendyolApiController {

//    private static final String getDetailedProductUrl = "https://api.trendyol.com/sapigw/suppliers/871469/products?barcode=";

//    private final TrendyolProductService trendyolProductService;
//    private final UserService userService;
    private final TrendyolApiService trendyolApiService;
//    private final ProductService productService;
//    private final RestTemplateTrendyolWithHeaders restTemplateTrendyolWithHeaders;

    @GetMapping("update-to-trendyol-product-quantity")
    public ResponseEntity<String> updateToTrendyolProductQuantityAndPrice(@AuthenticationPrincipal User user) {
       return  trendyolApiService.updateToTrendyolProductQuantity(user);
    }

//    @GetMapping("get-detail-product-from-trendyol/{barcode}")
//    private ResponseEntity<String> getDetailProductFromTrendyol(@AuthenticationPrincipal User user,@PathVariable String barcode){
//
//        // HTTP Headers oluşturma
//        HttpEntity<String> entity =restTemplateTrendyolWithHeaders.prepareHttpEntity(user);
//
//        // API'ye POST isteği gönderme
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(getDetailedProductUrl+barcode, HttpMethod.GET, entity, String.class);
//
//        // Yanıtı kontrol etme (opsiyonel)
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getBody());
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                // Gelen JSON'dan images kısmını çıkar
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode root = mapper.readTree(response.getBody());
//                JsonNode contentNode = root.path("content");
//                if (contentNode.isArray()) {
//                    for (JsonNode productNode : contentNode) {
//                        JsonNode imagesNode = productNode.path("images");
//
//                        List<String> images = new ArrayList<>();
//                        for (JsonNode imageNode : imagesNode) {
//                            images.add(imageNode.path("url").asText());
//                        }
//
//                        // Veritabanından ürünü bul
//                        Product product = productService.findByBarcode(barcode).orElseThrow();
//                        // Ürünün resimlerini güncelle
//                        product.setImagePath(images.get(0));
//                        productService.updateProduct(product);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        return response;
//    }

    @GetMapping("get-all-product-from-trendyol-and-update-db")
    private   List<TrendyolProducts> getAllProductFromTrendyolAndUpdateDb(@AuthenticationPrincipal User user) throws Exception {
        return trendyolApiService.getAllProductFromTrendyolAndUpdateMyDb(user);
    }
}




