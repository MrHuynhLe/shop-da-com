package com.example.shop.controller;

import com.example.shop.entity.Review;
import com.example.shop.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review add(@RequestParam Long productId, @RequestParam int rating, @RequestParam(required = false) String comment) {
        return reviewService.add(productId, rating, comment);
    }

    @GetMapping("/product/{id}")
    public List<Review> byProduct(@PathVariable("id") Long productId) {
        return reviewService.byProduct(productId);
    }
}
