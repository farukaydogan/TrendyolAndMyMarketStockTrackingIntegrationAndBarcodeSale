package com.fta.stock.tracking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fta.stock.tracking.helper.GetNowAndYesterdayTimestamp;
import com.fta.stock.tracking.helper.RestTemplateTrendyolWithHeaders;
import com.fta.stock.tracking.model.*;
import com.fta.stock.tracking.repository.TrendyolOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TrendyolOrderService {
    private final TrendyolOrderRepository trendyolOrderRepository;
    private final OrderDetailService orderDetailService;

    public TrendyolOrder save(TrendyolOrder trendyolOrder){
        trendyolOrder = trendyolOrderRepository.save(trendyolOrder);
        for(OrderDetail orderDetail : trendyolOrder.getOrderDetails()) {
            orderDetail.setTrendyolOrder(trendyolOrder);
            orderDetailService.createOrderDetail(orderDetail);
        }
        return trendyolOrder;
    }

}
