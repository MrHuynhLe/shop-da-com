package com.example.shop.service;

import com.example.shop.dto.OrderDtos.CreateOrderRequest;
import com.example.shop.dto.OrderDtos.OrderItemReq;
import com.example.shop.entity.*;
import com.example.shop.repository.OrderDetailRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public OrderService(OrderRepository orderRepo, OrderDetailRepository orderDetailRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.orderDetailRepo = orderDetailRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    @Transactional
    public Order create(CreateOrderRequest req) {
        User user = currentUser();
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalPrice(BigDecimal.ZERO)
                .build();
        List<OrderDetail> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemReq itemReq : req.items()) {
            var product = productRepo.findById(itemReq.productId()).orElseThrow();
            if (product.getStock() == null || product.getStock() < itemReq.quantity()) {
                throw new RuntimeException("Not enough stock for product " + product.getName());
            }
            product.setStock(product.getStock() - itemReq.quantity());
            productRepo.save(product);

            var od = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .price(product.getPrice())
                    .build();
            items.add(od);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
        }
        order.setItems(items);
        order.setTotalPrice(total);
        orderRepo.save(order);
        items.forEach(orderDetailRepo::save);
        return order;
    }

    public List<Order> myOrders(User user) {
        if (user.getRole() == Role.ROLE_ADMIN) return orderRepo.findAll();
        return orderRepo.findByUser(user);
    }

    public Order updateStatus(Long id, OrderStatus status) {
        var order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepo.save(order);
    }
}
