package com.fta.stock.tracking.service;

import com.fta.stock.tracking.model.CartItem;
import com.fta.stock.tracking.model.Order;
import com.fta.stock.tracking.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final TrendyolApiService trendyolApiService;
    private final ProductService productService;

    public Order createOrder(Order order) {

        return orderRepository.save(order);
    }

    public Optional<Order> findByOrderId(Integer orderId) {
        return orderRepository.findById(orderId.longValue());
    }

    public void finishOrderAndSave(Order order, List<CartItem> cartItems) {

        for (CartItem cartItem : cartItems) {
            productService.stockDecrease(cartItem.getProduct(), cartItem.getQuantity());
        }

        orderRepository.save(order);
    }
}