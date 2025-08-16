package com.example.shop.controller;

import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public User me(Authentication authentication) {
        return userRepo.findByUsername(authentication.getName()).orElseThrow();
    }
}
