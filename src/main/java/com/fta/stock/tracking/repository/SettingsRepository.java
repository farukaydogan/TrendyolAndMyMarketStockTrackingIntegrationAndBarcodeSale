package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.Notification;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findByUserId(Integer user_id);

}
