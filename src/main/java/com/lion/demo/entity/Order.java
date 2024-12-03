package com.lion.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Increment
    private long oid;
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order가 삭제되면 -> 관련한 OrderItem도 삭제됨
    private List<OrderItem> orderItems = new ArrayList<>(); // 빈 리스트로 초기화
    private LocalDateTime orderDateTime;
    private int totalAmount;

    // 연관 관계 메소드 추가
    public void addOrderItem(OrderItem orderItem) {
        if (this.orderItems == null)
            this.orderItems = new ArrayList<>();
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

}