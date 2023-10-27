package com.fta.stock.tracking.controller;

import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/management")
@RequiredArgsConstructor
public class AdminController {

    private final  UserService userService;
    @GetMapping()
    public String viewAdminPage(Model model) {
//        model.addAttribute("allemplist", employeeServiceImpl.getAllEmployee());
        List<User> users=userService.findAll();
        model.addAttribute("users", users);  //
        return "admin";
    }


}
