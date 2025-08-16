package com.example.shop.controller;

import com.example.shop.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/daily")
    public Map<String, Object> daily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return statisticsService.daily(date);
    }

    @GetMapping("/monthly")
    public Map<String, Object> monthly(@RequestParam int year, @RequestParam int month) {
        return statisticsService.monthly(year, month);
    }

    @GetMapping("/top-products")
    public List<Map<String, Object>> top(@RequestParam(defaultValue = "5") int limit) {
        return statisticsService.topProducts(limit);
    }
}
