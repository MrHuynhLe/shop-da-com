package com.example.shop.repository;

import com.example.shop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT od.product.id, SUM(od.quantity) as qty FROM OrderDetail od GROUP BY od.product.id ORDER BY qty DESC")
    List<Object[]> topProducts();
}
