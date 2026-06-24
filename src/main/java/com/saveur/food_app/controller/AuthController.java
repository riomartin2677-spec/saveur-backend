package com.saveur.food_app.controller;

import com.saveur.food_app.model.UserRole;
import com.saveur.food_app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "https://saveur-frontend.vercel.app"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> req) {
        String role = req.getOrDefault("role", "CUSTOMER");
        return authService.register(
            req.get("name"),
            req.get("email"),
            req.get("password"),
            UserRole.valueOf(role)
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {
        return authService.login(
            req.get("email"),
            req.get("password")
        );
    }
}