package com.lion.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStat {
    private long bid;
    private String title;
    private String company;
    private int unitPrice;
    private int quantity;
    private int totalPrice;
}