package com.lion.demo.service;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.entity.OrderItem;
import com.lion.demo.entity.User;
import com.lion.demo.repository.BookRepository;
import com.lion.demo.repository.CartRepository;
import com.lion.demo.repository.OrderRepository;
import com.lion.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Order createOrder(String uid, List<Cart> cartList) {
        User user = userRepository.findById(uid).orElse(null);
        Order order = Order.builder()
                .user(user)
                .orderDateTime(LocalDateTime.now())
                .build();

        int totalAmount = 0;
        // 카트 리스트를 orderItem(주문 상세)으로 가져와 Order로 추가 (orderItem - 각 상품별 주문 아이템(주문상세), order - 총 주문)
        for (Cart cart : cartList) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .book(cart.getBook())
                    .quantity(cart.getQuantity())
                    .subPrice(cart.getBook().getPrice()*cart.getQuantity())
                    .build();
            totalAmount += orderItem.getSubPrice();
            order.addOrderItem(orderItem);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        // 장바구니를 주문한 후 장바구니 비우기
        cartRepository.deleteAll(cartList);

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUser(String uid) {
        return orderRepository.findByUserUid(uid);
    }

    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByOrderDateTimeBetween(start, end);
    }
}
