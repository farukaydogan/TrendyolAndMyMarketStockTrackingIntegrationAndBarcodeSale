package com.fta.stock.tracking.controller.api;


import com.fta.stock.tracking.model.*;
import com.fta.stock.tracking.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final TrendyolApiService trendyolApiService;
    private final ProductService productService;

    @GetMapping("{orderId}")
    public ResponseEntity<?> getBasketWithId(@PathVariable Integer orderId) {
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        return ResponseEntity.ok(order);
    }


    @PostMapping
    public ResponseEntity<String> basketAdd(@RequestBody List<CartItem> cartItems,
                                       @AuthenticationPrincipal User user) throws IOException {

        if (cartItems.isEmpty()) {
            String failedString = new JSONObject()
                    .put("message", "Sepet Boş olamaz")
                    .put("data", false)
                    .toString();

            return new ResponseEntity<String>(failedString, HttpStatus.NOT_FOUND);
        }

//        burada urunler kontrol ediliyor var mi diye
        trendyolApiService.getTrendyolOrderAndUpdateDecreaseStock(user);



        // Yeni bir sipariş oluştur
        Order order = new Order();
        order.setUser(user);
        order.setStatus("Processing"); // Varsayılan durum
        double totalPriceOrder = 0;
        // Siparişi veritabanına kaydet
        order = orderService.createOrder(order);

        // Her bir sepet ürünü için
        for (CartItem cartItem : cartItems) {

            if (!productService.checkQuantity(cartItem)) {
                String failedString = new JSONObject()
                        .put("message", "Urun Stokta yok")
                        .put("data", false)
                        .toString();

                return new ResponseEntity<String>(failedString, HttpStatus.NOT_FOUND);
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
//        TODO BURASI SONRA ACILMASI GEREKIYOR
//            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            double orderDetailPrice = cartItem.getProduct().getPrice();
            orderDetail.setPrice(orderDetailPrice); // Ürün fiyatını kullanıyoruz, bu indirimler vb. için değiştirilebilir.

    //            burada hibernet useri yukelemedigi icin product stogu duserken useri null birakmamasi icin user tanimlaniyor
            cartItem.getProduct().setUser(user);
            totalPriceOrder = totalPriceOrder + orderDetailPrice;
            // Sipariş detayını veritabanına kaydet
            orderDetailService.createOrderDetail(orderDetail);
        }


        order.setTotalPrice(totalPriceOrder);

        orderService.finishOrderAndSave(order,cartItems);


        String jsonString = new JSONObject()
                .put("message", "Order created")
                .put("data", true)
                .toString();

        return new ResponseEntity<String>(jsonString, HttpStatus.CREATED);
    }
}
