package com.example.shop.controller;

import com.example.shop.dto.OrderDtos.CreateOrderRequest;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderStatus;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepo;

    public OrderController(OrderService orderService, UserRepository userRepo) {
        this.orderService = orderService;
        this.userRepo = userRepo;
    }

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) {
        return orderService.create(req);
    }

    @GetMapping
    public List<Order> myOrders(Authentication authentication) {
        User user = userRepo.findByUsername(authentication.getName()).orElseThrow();
        return orderService.myOrders(user);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Order updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}
