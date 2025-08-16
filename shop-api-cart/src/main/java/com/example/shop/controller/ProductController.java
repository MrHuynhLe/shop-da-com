package com.example.shop.controller;

import com.example.shop.dto.ProductDtos.*;
import com.example.shop.entity.Role;
import com.example.shop.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> all() { return productService.findAll(); }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ProductResponse create(@RequestBody ProductRequest req) { return productService.create(req); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ProductResponse update(@PathVariable Long id, @RequestBody ProductRequest req) { return productService.update(id, req); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) { productService.delete(id); }
}
