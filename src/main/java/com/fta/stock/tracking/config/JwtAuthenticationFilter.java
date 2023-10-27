package com.fta.stock.tracking.config;

import com.fta.stock.tracking.repository.TokenRepository;
import com.fta.stock.tracking.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// ... diğer importlar

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    if (request.getServletPath().contains("/api/v1/auth") ||
            request.getServletPath().contains("/auth")||
            request.getServletPath().contains("/swagger")||
            request.getServletPath().contains("/api-docs")) {
      filterChain.doFilter(request, response);
      return;
    }



    String jwt = null;


    // Öncelikle JWT tokenini cookie'den alalım.
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("jwt".equals(cookie.getName())) {
          jwt = cookie.getValue();
          break;
        }
      }
    }

    // Eğer cookie'de JWT bulamazsak, 'Authorization' header'ına bakalım.
    if (jwt == null) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        jwt = authHeader.substring(7);
      }
    }

    // Eğer cookie'de de, header'da da JWT bulamazsak, "Please Login" mesajını döndürelim.
    if (jwt == null) {
//      response.setStatus(HttpStatus.FORBIDDEN.value());
//      response.setContentType("application/json");
//      response.getWriter().write("{\"message\":\"Please Login\"}");
//      return;
      response.sendRedirect("/auth/login");  // '/login' adresine yönlendirme yapar.
      return;
    }



    String userEmail = jwtService.extractUsername(jwt);
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked())
              .orElse(false);

      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
