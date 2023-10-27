package com.fta.stock.tracking.service;

import com.fta.stock.tracking.helper.TrendyolEndpoint;
import com.fta.stock.tracking.model.Settings;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.repository.SettingsRepository;
import com.fta.stock.tracking.requests.UpdateSettingsRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;
    private final UserService userService;

    public Settings findByUserId(Integer userId) {
        return settingsRepository.findByUserId(userId);
    }

    public Settings saveSettings(Settings settings) {

        return settingsRepository.save(settings);
    }

    public boolean isScheduleEnabled(String userId) {
        Settings settings = settingsRepository.findByUserId(Integer.valueOf(userId));
        return settings.getEnableScheeduling();
    }

    public User updateSettings(User user, UpdateSettingsRequest updatedUserSettings) {
        Settings currentSettings = findByUserId(user.getId());

        if (updatedUserSettings.getTrendyolApiKey() != null && !updatedUserSettings.getTrendyolApiKey().trim().isEmpty()) {
            currentSettings.setTrendyolApiKey(updatedUserSettings.getTrendyolApiKey());
        }
        if (updatedUserSettings.getTrendyolUserId() != null && !updatedUserSettings.getTrendyolUserId().trim().isEmpty()) {
            currentSettings.setTrendyolUserId(updatedUserSettings.getTrendyolUserId());
        }

        // Eğer SettingsService'de bir update metodu varsa, aşağıdaki satırı kullanabilirsiniz.
        // Aksi halde, Settings nesnesini JpaRepository.save() metodu ile güncelleyebilirsiniz.
        currentSettings = settingsRepository.save(currentSettings);

        user.setSettings(currentSettings);

        if (updatedUserSettings.getEmail() != null && !updatedUserSettings.getEmail().trim().isEmpty()) {
            user.setEmail(updatedUserSettings.getEmail());
        }
        if (updatedUserSettings.getPassword() != null && !updatedUserSettings.getPassword().trim().isEmpty()) {
            user.setPassword(updatedUserSettings.getPassword());
        }
        if (updatedUserSettings.getNameSurname() != null && !updatedUserSettings.getNameSurname().trim().isEmpty()) {
            user.setFirstname(updatedUserSettings.getNameSurname());
        }

        return userService.saveUser(user);
    }


    public String createEndpointForUser(String supplierId, TrendyolEndpoint endpointType) {
        // Veritabanından kullanıcıya özel ayarları çekmek için gerekli kodu buraya ekleyin
        String apiUrl = TrendyolEndpoint.BASE_URL.getEndpoint();
        String endpoint = endpointType.getEndpoint();

        return apiUrl + supplierId + endpoint;
    }



}
