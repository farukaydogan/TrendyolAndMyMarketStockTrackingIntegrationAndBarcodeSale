package com.fta.stock.tracking.controller;


import com.fta.stock.tracking.exceptions.ApiRequestException;
import com.fta.stock.tracking.model.Settings;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.requests.UpdateSettingsRequest;
import com.fta.stock.tracking.service.SettingsService;
import com.fta.stock.tracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SettingsService settingsService;

    @GetMapping("")
    public String getSettings(@AuthenticationPrincipal User user, Model model)
    {
        Settings settings=settingsService.findByUserId(user.getId());

        model.addAttribute("settings",settings);
        model.addAttribute("user",user);

        return "settings";
    }

}
