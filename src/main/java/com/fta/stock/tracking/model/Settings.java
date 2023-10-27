package com.fta.stock.tracking.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_settings")
public class Settings {

    @Id
    @GeneratedValue
    private Integer settingsId;

    private String trendyolApiKey;

    private Boolean enableScheeduling;

    private String trendyolUserId;

    @OneToOne
    @JoinColumn(name = "userId")
    @Hidden
    @JsonIgnore
    private User user;

}
