package com.fta.stock.tracking.helper;

import com.fta.stock.tracking.model.Settings;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestTemplateTrendyolWithHeaders {
    private final SettingsService settingsService;

    public HttpEntity<String> prepareHttpEntity(User user) {
        String trendyolAuthToken = settingsService.findByUserId(user.getId()).getTrendyolApiKey();
        // HTTP Headers oluşturma
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + trendyolAuthToken);
        headers.set("User-Agent", "200300444 - Trendyolsoft");
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>(headers);
    }
}
