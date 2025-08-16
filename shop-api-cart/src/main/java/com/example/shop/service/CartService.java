package com.example.shop.service;

import com.example.shop.entity.*;
import com.example.shop.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderDetailRepository orderDetailRepo;

    public CartService(CartRepository cartRepo, CartItemRepository cartItemRepo, UserRepository userRepo,
                       ProductRepository productRepo, OrderRepository orderRepo, OrderDetailRepository orderDetailRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderDetailRepo = orderDetailRepo;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    public Cart getMyCart() {
        User user = currentUser();
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = Cart.builder().user(user).items(new ArrayList<>()).build();
            return cartRepo.save(c);
        });
    }

    @Transactional
    public Cart add(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        Cart cart = getMyCart();
        var product = productRepo.findById(productId).orElseThrow();
        var item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId).orElse(null);
        if (item == null) {
            item = CartItem.builder().cart(cart).product(product).quantity(quantity).build();
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartItemRepo.save(item);
        return getMyCart();
    }

    @Transactional
    public Cart update(Long productId, int quantity) {
        Cart cart = getMyCart();
        var item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));
        if (quantity <= 0) {
            cartItemRepo.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }
        return getMyCart();
    }

    @Transactional
    public Cart remove(Long productId) {
        Cart cart = getMyCart();
        cartItemRepo.findByCartIdAndProductId(cart.getId(), productId).ifPresent(cartItemRepo::delete);
        return getMyCart();
    }

    @Transactional
    public Cart clear() {
        Cart cart = getMyCart();
        cart.getItems().forEach(ci -> cartItemRepo.deleteById(ci.getId()));
        return getMyCart();
    }

    @Transactional
    public Order checkout() {
        Cart cart = getMyCart();
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Order order = Order.builder()
                .user(cart.getUser())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalPrice(BigDecimal.ZERO)
                .build();
        BigDecimal total = BigDecimal.ZERO;
        var items = new ArrayList<OrderDetail>();
        for (var ci : cart.getItems()) {
            var p = ci.getProduct();
            if (p.getStock() == null || p.getStock() < ci.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + p.getName());
            }
            p.setStock(p.getStock() - ci.getQuantity());
            productRepo.save(p);

            var od = OrderDetail.builder()
                    .order(order).product(p).quantity(ci.getQuantity()).price(p.getPrice())
                    .build();
            items.add(od);
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        order.setItems(items);
        order.setTotalPrice(total);
        orderRepo.save(order);
        items.forEach(orderDetailRepo::save);

        // clear cart
        cart.getItems().forEach(ci -> cartItemRepo.deleteById(ci.getId()));
        return order;
    }
}
