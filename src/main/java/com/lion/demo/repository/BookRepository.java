package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 페이지 단위로 데이터 조회
    Page<Book> findAll(Pageable pageable);

    // 해당 제목의 책 검색
    // select * from Book where title like %검색어%;
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByAuthorContaining(String author, Pageable pageable);
    Page<Book> findByCompanyContaining(String company, Pageable pageable);
    Page<Book> findBySummaryContaining(String summary, Pageable pageable);

    Page<Book> findByPriceBetween(int minPrice, int maxPrice, Pageable pageable);
}
