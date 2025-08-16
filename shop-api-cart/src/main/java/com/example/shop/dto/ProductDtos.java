package com.example.shop.dto;

import java.math.BigDecimal;

public class ProductDtos {
    public record ProductRequest(String name, BigDecimal price, Integer stock, Long categoryId, String imageUrl) {}
    public record ProductResponse(Long id, String name, BigDecimal price, Integer stock, String category, String imageUrl) {}
}
