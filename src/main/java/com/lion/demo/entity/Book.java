package com.lion.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book{
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private long bid;
    private String title;
    private String author;
    private String company;
    private int price;
    private String imageUrl;
    @Column(length = 8191)  // column 길이 (default:256 -> 8191)
    private String summary;
}
