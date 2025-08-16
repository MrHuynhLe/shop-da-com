package com.example.shop.controller;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Order;
import com.example.shop.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart get() { return cartService.getMyCart(); }

    @PostMapping("/add")
    public Cart add(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity) {
        return cartService.add(productId, quantity);
    }

    @PutMapping("/update")
    public Cart update(@RequestParam Long productId, @RequestParam int quantity) {
        return cartService.update(productId, quantity);
    }

    @DeleteMapping("/remove")
    public Cart remove(@RequestParam Long productId) {
        return cartService.remove(productId);
    }

    @DeleteMapping("/clear")
    public Cart clear() { return cartService.clear(); }

    @PostMapping("/checkout")
    public Order checkout() { return cartService.checkout(); }
}
