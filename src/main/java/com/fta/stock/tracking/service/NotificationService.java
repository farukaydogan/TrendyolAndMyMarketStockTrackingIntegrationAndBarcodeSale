package com.fta.stock.tracking.service;

import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public void checkStockQuantity(List<Product> productList) throws MessagingException {

        for (Product product : productList) {
            Integer stockQuantity=product.getStockQuantity();
            if (stockQuantity< 100) {
                emailService.sendEmail("Ürün Stoğu az kaldı stok: "+stockQuantity,"Stok bildirimi");
            }
        }
    }
}
