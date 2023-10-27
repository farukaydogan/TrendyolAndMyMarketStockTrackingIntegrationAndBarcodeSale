package com.fta.stock.tracking.service;

import com.fta.stock.tracking.model.Order;
import com.fta.stock.tracking.model.OrderDetail;
import com.fta.stock.tracking.repository.OrderDetailRepository;
import com.fta.stock.tracking.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public void createOrderDetail(OrderDetail orderDetail){

         orderDetailRepository.save(orderDetail);
    }
}
