package com.example.shop.service;

import com.example.shop.dto.ProductDtos.*;
import com.example.shop.entity.Category;
import com.example.shop.entity.Product;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<ProductResponse> findAll() {
        return productRepo.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock(),
                        p.getCategory() != null ? p.getCategory().getName() : null, p.getImageUrl()))
                .toList();
    }

    public ProductResponse create(ProductRequest req) {
        Category category = null;
        if (req.categoryId() != null) {
            category = categoryRepo.findById(req.categoryId()).orElseThrow();
        }
        Product p = Product.builder()
                .name(req.name())
                .price(req.price())
                .stock(req.stock())
                .category(category)
                .imageUrl(req.imageUrl())
                .build();
        productRepo.save(p);
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock(),
                category != null ? category.getName() : null, p.getImageUrl());
    }

    public ProductResponse update(Long id, ProductRequest req) {
        var p = productRepo.findById(id).orElseThrow();
        p.setName(req.name());
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setImageUrl(req.imageUrl());
        if (req.categoryId() != null) {
            var c = categoryRepo.findById(req.categoryId()).orElseThrow();
            p.setCategory(c);
        } else {
            p.setCategory(null);
        }
        productRepo.save(p);
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock(),
                p.getCategory() != null ? p.getCategory().getName() : null, p.getImageUrl());
    }

    public void delete(Long id) { productRepo.deleteById(id); }
}
