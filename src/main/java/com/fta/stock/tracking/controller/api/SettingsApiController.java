package com.fta.stock.tracking.controller.api;

import com.fta.stock.tracking.model.Settings;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.requests.UpdateSettingsRequest;
import com.fta.stock.tracking.service.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingsApiController {
    private final SettingsService settingsService;

    @PostMapping()
    public ResponseEntity<String> updateSettings(@AuthenticationPrincipal User user, @RequestBody UpdateSettingsRequest settings) {
        try {
            User updatedUser = settingsService.updateSettings(user, settings);
            if (updatedUser != null) {
                JSONObject successResponse = new JSONObject()
                        .put("message", "Kullanıcı başarıyla güncellendi.")
                        .put("data", true);
                return new ResponseEntity<>(successResponse.toString(), HttpStatus.OK);
            } else {
                JSONObject failedResponse = new JSONObject()
                        .put("message", "Kullanıcı güncellenemedi.")
                        .put("data", false);
                return new ResponseEntity<>(failedResponse.toString(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            JSONObject errorResponse = new JSONObject()
                    .put("message", "Bir hata oluştu: " + e.getMessage())
                    .put("data", false);
            return new ResponseEntity<>(errorResponse.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
