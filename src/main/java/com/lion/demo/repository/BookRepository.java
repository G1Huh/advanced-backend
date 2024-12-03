package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 페이지 단위로 데이터 조회
    Page<Book> findAll(Pageable pageable);
}
