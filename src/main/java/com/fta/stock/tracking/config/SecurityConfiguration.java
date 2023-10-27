package com.fta.stock.tracking.config;

import com.fta.stock.tracking.exceptions.CustomAccessDeniedHandler;
import com.fta.stock.tracking.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.fta.stock.tracking.model.Permission.*;
import static com.fta.stock.tracking.model.Role.ADMIN;
import static com.fta.stock.tracking.model.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // CSRF korumasını devre dışı bırak
            .csrf().disable()

            // Oturum yönetimi (stateless olacak şekilde)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // Rotalar için yetkilendirme
            .authorizeRequests()

            // Auth ve Swagger için özel izinler
            .requestMatchers(
                    "/api/v1/auth/**",
                    "/auth/**",
                    "/v2/api-docs",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/swagger-ui.html"
            ).permitAll()

            // Management yolları için yetkilendirme (rol ve yetki bazlı)
            .requestMatchers("/api/v1/management/**","/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
            .requestMatchers(GET, "/api/v1/management/**","/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
            .requestMatchers(POST, "/api/v1/management/**","/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
            .requestMatchers(PUT, "/api/v1/management/**","/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
            .requestMatchers(DELETE, "/api/v1/management/**","/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

            // Diğer tüm yollar için kimlik doğrulaması
            .anyRequest().authenticated()

            .and()

            // Kimlik doğrulama ayarları
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
            // Çıkış işlemi ayarları
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
//

    return http.build();
  }
}