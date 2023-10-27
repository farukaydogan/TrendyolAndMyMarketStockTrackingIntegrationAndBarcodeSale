package com.fta.stock.tracking.controller;



import com.fta.stock.tracking.auth.AuthenticationRequest;
import com.fta.stock.tracking.auth.AuthenticationResponse;
import com.fta.stock.tracking.auth.AuthenticationService;
import com.fta.stock.tracking.auth.RegisterRequest;
import com.fta.stock.tracking.exceptions.ApiRequestException;
import com.fta.stock.tracking.model.Role;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    private final UserService userService;  // Kullanıcı işlemleri için bir servis olmalı


    @GetMapping("/login")
    public String viewHomePage() {
//        model.addAttribute("allemplist", employeeServiceImpl.getAllEmployee());
        return "login";
    }



    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response) {
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        try {
            AuthenticationResponse authResponse = authenticationService.authenticate(request);

            // Token'ı bir cookie olarak kaydet
            Cookie jwtCookie = new Cookie("jwt", authResponse.getAccessToken());
            jwtCookie.setSecure(true);  // HTTPS üzerinden gönder
            jwtCookie.setHttpOnly(false);  // JavaScript ile erişilemez
            jwtCookie.setPath("/");  // Cookie'nin geçerli olacağı yol
            response.addCookie(jwtCookie);


            return "redirect:/";  // index sayfasına yönlendir
        } catch (ApiRequestException e) {
            // Hata durumlarında ne yapılacağı (Örneğin: hata sayfasına yönlendirme)
            return "redirect:/login?error=true";
        }
    }



    @GetMapping("/register")
    public String viewRegisterPage() {
        return "register";
    }


    @PostMapping("/register")
    public String register(@RequestParam String firstname,
                           @RequestParam String lastname,
                           @RequestParam String email,
                           @RequestParam String password,
                           HttpServletResponse response) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setFirstname(firstname);
        request.setLastname(lastname);
        request.setRole(Role.ADMIN);

        try {
            AuthenticationResponse authResponse = authenticationService.register(request);

            // Token'ı bir cookie olarak kaydet
            Cookie jwtCookie = new Cookie("jwt", authResponse.getAccessToken());
            jwtCookie.setSecure(true);  // HTTPS üzerinden gönder
            jwtCookie.setHttpOnly(true);  // JavaScript ile erişilemez
            jwtCookie.setPath("/");  // Cookie'nin geçerli olacağı yol
            response.addCookie(jwtCookie);

            return "redirect:/";  // Başarılı kayıttan sonra ana sayfaya yönlendir.
        } catch (ApiRequestException e) {
            // Hata durumlarında ne yapılacağı
            // Örneğin, hata sayfasına yönlendirme veya error mesajını kullanıcıya gösterme.
            return "redirect:/register?error=true";
        }
    }
}
