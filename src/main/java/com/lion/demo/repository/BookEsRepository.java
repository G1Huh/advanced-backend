package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.BookEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookEsRepository extends ElasticsearchRepository<BookEs, String> {
    // 페이지 단위로 데이터 조회
    Page<BookEs> findAll(Pageable pageable);

    // 해당 제목의 책 검색
    // select * from Book where title like %검색어%;
    Page<BookEs> findByTitleContaining(String title, Pageable pageable);
    Page<BookEs> findByAuthorContaining(String author, Pageable pageable);
    Page<BookEs> findByCompanyContaining(String company, Pageable pageable);
    Page<BookEs> findBySummaryContaining(String summary, Pageable pageable);

    Page<BookEs> findByPriceBetween(int minPrice, int maxPrice, Pageable pageable);
}
