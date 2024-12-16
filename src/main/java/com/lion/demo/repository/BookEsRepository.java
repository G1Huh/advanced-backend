package com.lion.demo.repository;

import com.lion.demo.entity.BookEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookEsRepository extends ElasticsearchRepository<BookEs, String> {
    Page<BookEs> findAll(Pageable pageable);

    // select * from bookEs where title like '%title%'
    Page<BookEs> findByTitleContaining(String title, Pageable pageable);

    Page<BookEs> findByAuthorContaining(String author, Pageable pageable);
    Page<BookEs> findByCompanyContaining(String company, Pageable pageable);
    Page<BookEs> findBySummaryContaining(String summary, Pageable pageable);

    Page<BookEs> findByPriceBetween(int minPrice, int maxPrice, Pageable pageable);
}