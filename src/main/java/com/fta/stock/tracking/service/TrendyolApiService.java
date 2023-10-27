package com.fta.stock.tracking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fta.stock.tracking.helper.GetNowAndYesterdayTimestamp;
import com.fta.stock.tracking.helper.RestTemplateTrendyolWithHeaders;
import com.fta.stock.tracking.helper.TrendyolEndpoint;
import com.fta.stock.tracking.model.*;
import com.fta.stock.tracking.repository.TrendyolOrderRepository;
import com.fta.stock.tracking.repository.TrendyolProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrendyolApiService {
    private final ProductService productService;
    private final TrendyolProductService trendyolProductService;
    private final RestTemplateTrendyolWithHeaders restTemplateTrendyolWithHeaders;
    private final TrendyolOrderRepository trendyolOrderRepository;
    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final TrendyolOrderService trendyolOrderService;
    private final SettingsService settingsService;

//        TODO BURASI SONRA KULLANICILAR ICIN DUZELTILECEK
//    private static final String updatePriceAndAmountUrl = "https://api.trendyol.com/sapigw/suppliers/871469/products/price-and-inventory";
//    private static final String getAllProductUrl = "https://api.trendyol.com/sapigw/suppliers/871469/products?size=100";
    //        TODO BURASI SONRA KULLANICILAR ICIN DUZELTILECEK


    public ResponseEntity<String>  updateToTrendyolProductQuantity(User user) {

//        TODO BURASI SONRA KULLANICILAR ICIN DUZELTILECEK
        List<Product> allProducts = productService.findAll(); // Tüm ürünleri getir
        String trendyolUserId=settingsService.findByUserId(user.getId()).getTrendyolUserId();
        String url = settingsService.createEndpointForUser(trendyolUserId, TrendyolEndpoint.UPDATE_PRICE_AND_AMOUNT);

        RestTemplate restTemplate = new RestTemplate();

        // HTTP Headers oluşturma

        HttpEntity<String> entity = restTemplateTrendyolWithHeaders.prepareHttpEntity(user);

        // `TrendyolProduct` listesini JSON'a dönüştür
        StringBuilder itemsJson = new StringBuilder("[");

        for(
                Product product :allProducts)

        {
            List<TrendyolProducts> trendyolProducts = product.getTrendyolProducts();

            for (TrendyolProducts trendyolProduct : trendyolProducts) {
                itemsJson.append("{")
                        .append("\"barcode\": \"").append(trendyolProduct.getBarcode()).append("\",")
                        .append("\"quantity\": ").append(trendyolProduct.getProduct().getStockQuantity())
                        .append("},");
            }
        }

        // Eğer itemsJson sonunda "," ile bitiyorsa, bunu kaldıralım
        if(itemsJson.charAt(itemsJson.length()-1)==',')

        {
            itemsJson.setLength(itemsJson.length() - 1);
        }

        itemsJson.append("]");

        String body = "{\"items\": " + itemsJson.toString() + "}";


        // API'ye POST isteği gönderme
        return     restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, entity.getHeaders()), String.class);

    }

    public void getTrendyolOrderAndUpdateDecreaseStock(User user) throws IOException {

        GetNowAndYesterdayTimestamp getNowAndYesterdayTimestamp = new GetNowAndYesterdayTimestamp();
        long now=getNowAndYesterdayTimestamp.getNow();
        long yesterday= getNowAndYesterdayTimestamp.getYesterday();

//        get endpoint url prepare
        String trendyolUserId=settingsService.findByUserId(user.getId()).getTrendyolUserId();
        String url = settingsService.createEndpointForUser(trendyolUserId, TrendyolEndpoint.GET_ALL_ORDERS);

        //prepare headers
        HttpEntity<String> entity =restTemplateTrendyolWithHeaders.prepareHttpEntity(user);


        // API'ye POST isteği gönderme
        RestTemplate restTemplate = new RestTemplate();


//        bugun ve dunun siparisleri filtreleniyor
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("size","200")
                .queryParam("startDate",yesterday )
                .queryParam("endDate", now);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        List<TrendyolOrder> orders = new ArrayList<>();

        for (JsonNode orderNode : rootNode.path("content")) {
//            eger veritabaninda varsa pass gec
            String orderNumber = orderNode.get("orderNumber").asText();
            if (trendyolOrderRepository.findByOrderNumber(orderNumber) != null) {
                continue;
            }
            TrendyolOrder trendyolOrder = new TrendyolOrder();
            trendyolOrder.setTotalPrice(orderNode.path("totalPrice").asDouble());
            trendyolOrder.setOrderNumber(orderNode.path("orderNumber").asText());

//            gmt+3 eklememsi icin yapiliyor
            Long orderTime=orderNode.path("orderDate").asLong();
            trendyolOrder.setOrderDate(getNowAndYesterdayTimestamp.returnThreeHourBefore(orderTime));

            trendyolOrder.setStatus(orderNode.get("status").toString());
            trendyolOrder.setUser(user);
            trendyolOrder = trendyolOrderRepository.save(trendyolOrder); // Save TrendyolOrder first

//            trendyolOrder = save(trendyolOrder);
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (JsonNode lineNode : orderNode.path("lines")) {
                OrderDetail orderDetail = new OrderDetail();
                Integer quantity = lineNode.path("quantity").asInt();
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(lineNode.path("amount").asDouble());
                orderDetail.setTrendyolOrder(trendyolOrder);
                Product product = productService.findByBarcode(lineNode.path("merchantSku").asText()).orElseThrow(() -> new RuntimeException("Product not found"));
                orderDetail.setProduct(product);
                orderDetailService.createOrderDetail(orderDetail);
//                stoktan dus
                productService.stockDecrease(product, quantity);
                orderDetails.add(orderDetail);
            }
            trendyolOrder.setOrderDetails(orderDetails);
            trendyolOrder = trendyolOrderService.save(trendyolOrder);
            orders.add(trendyolOrder);
        }
    }


    public  List<TrendyolProducts>  getAllProductFromTrendyolAndUpdateMyDb(User user) throws Exception {
        // HTTP Headers oluşturma
        HttpEntity<String> entity =restTemplateTrendyolWithHeaders.prepareHttpEntity(user);

        // API'ye POST isteği gönderme
        RestTemplate restTemplate = new RestTemplate();

//        url preparing
        String trendyolUserId=settingsService.findByUserId(user.getId()).getTrendyolUserId();
        String url = settingsService.createEndpointForUser(trendyolUserId, TrendyolEndpoint.GET_ALL_PRODUCT);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        TODO BURASI SONRA KULLANICILAR ICIN DUZELTILECEK

        // Response'u parse etmek için TrendyolService sınıfını kullanma
        List<TrendyolProducts> productList = trendyolProductService.parseTrendyolProductResponse(response.getBody());
        trendyolProductService.flushTableAndSaveProduct(productList,user);
        return productList;
    }


}
