package com.fta.stock.tracking.controller;


import com.fta.stock.tracking.auth.AuthenticationRequest;
import com.fta.stock.tracking.auth.AuthenticationResponse;
import com.fta.stock.tracking.exceptions.ApiRequestException;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.ProductService;
import com.fta.stock.tracking.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



    @GetMapping("edit")
    public String addProduct(@AuthenticationPrincipal User user, Model model) {

        List<Product> productList=user.getProducts();
        // ID'ye göre sıralama
        productList.sort(Comparator.comparing(Product::getProductId));
        model.addAttribute("products",productList);
        return "productEdit";
    }



    @GetMapping("add")
    public String editProduct(@AuthenticationPrincipal User user, Model model) {
//        List<Device> devices=user.getDevices();
//        model.addAttribute("devices",devices);
        //        List<User> users=userService.findAll();
        //        model.addAttribute("users", users);  //
        model.addAttribute("activePage", "product");
        return "productAdd";
    }




    @PostMapping("/add")
    public String login(Product product,
                        HttpServletResponse response,
                        @AuthenticationPrincipal User user, Model model
                        ) {
        try {

            product.setUser(user);

            if (productService.saveProduct(product)==null){
                model.addAttribute("message", "Ürün başarıyla eklendi");
                model.addAttribute("messageType", "success");
            }
            else {
                model.addAttribute("message", "Kullanıcı bulunamadı");
                model.addAttribute("messageType", "danger");
            }
            return "productEdit"; // Hata durumunda yönlendirilecek sayfa
        } catch (ApiRequestException e) {
            // Hata durumlarında ne yapılacağı (Örneğin: hata sayfasına yönlendirme)
            model.addAttribute("message", "Bir hata oluştu: " + e.getMessage());
            model.addAttribute("messageType", "danger");
            return "productEdit"; // Hata durumunda yönlendirilecek sayfa

        }
    }
}
