package com.example.shop.dto;

import java.util.List;

public class OrderDtos {
    public record OrderItemReq(Long productId, Integer quantity) {}
    public record CreateOrderRequest(List<OrderItemReq> items) {}
}
