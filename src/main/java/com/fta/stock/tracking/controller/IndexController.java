package com.fta.stock.tracking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final  UserService userService;
    @GetMapping()
    public String viewAdminPage(@AuthenticationPrincipal User user, Model model) throws JsonProcessingException {

        List<Product> productList=user.getProducts();

        ObjectMapper objectMapper = new ObjectMapper();
        String productsJson = objectMapper.writeValueAsString(productList);
        model.addAttribute("products", productsJson);
        return "index";
    }


}
