package com.lion.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "books")  // elasticsearch
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEs {
    @Id
    private String bookId; // UUID style
    private String title;
    private String author;
    private String company;
    private int price;
    private String imageUrl;
    private String summary;

}
