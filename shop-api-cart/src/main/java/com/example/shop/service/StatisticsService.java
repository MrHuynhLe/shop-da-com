package com.example.shop.service;

import com.example.shop.repository.OrderDetailRepository;
import com.example.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticsService {
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;

    public StatisticsService(OrderRepository orderRepo, OrderDetailRepository orderDetailRepo) {
        this.orderRepo = orderRepo;
        this.orderDetailRepo = orderDetailRepo;
    }

    public Map<String, Object> daily(LocalDate date) {
        BigDecimal revenue = Optional.ofNullable(orderRepo.revenueByDay(date)).orElse(BigDecimal.ZERO);
        return Map.of("date", date.toString(), "revenue", revenue);
    }

    public Map<String, Object> monthly(int year, int month) {
        BigDecimal revenue = orderRepo.revenueByMonth(year, month);
        return Map.of("year", year, "month", month, "revenue", revenue);
    }

    public List<Map<String, Object>> topProducts(int limit) {
        var rows = orderDetailRepo.topProducts();
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> m = new HashMap<>();
            m.put("productId", row[0]);
            m.put("quantity", row[1]);
            out.add(m);
        }
        if (out.size() > limit) return out.subList(0, limit);
        return out;
    }
}
