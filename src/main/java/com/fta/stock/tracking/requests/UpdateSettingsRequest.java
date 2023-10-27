package com.fta.stock.tracking.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateSettingsRequest {
    private String nameSurname;
    private String trendyolApiKey;
    private String trendyolUserId;
    private String email;
    private String password;
}
