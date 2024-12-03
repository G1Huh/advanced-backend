package com.lion.demo.service;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order createOrder(String uid, List<Cart> cartList); // JPA Repository에 Save하면(Insert,Update) 해당 객체를 반환
    List<Order> getOrdersByUser(String uid);
    List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end);
}
