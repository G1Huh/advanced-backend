package com.lion.demo.repository;

import com.lion.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 개별 사용자 - 본인의 주문 조회
    List<Order> findByUserUid(String uid);

    // 관리자 - 기간 설정하여 주문 조회
    List<Order> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
