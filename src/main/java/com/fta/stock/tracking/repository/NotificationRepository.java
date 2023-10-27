package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Custom query methods if needed
}
