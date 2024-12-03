package com.lion.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id // primary-key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Increment
    private long iid; // item Id

    @ManyToOne
    @JoinColumn(name = "oid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bid")
    private Book book;
    private int quantity;
    private int subPrice;
}
