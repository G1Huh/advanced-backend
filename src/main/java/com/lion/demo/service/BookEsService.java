package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.BookEs;
import com.lion.demo.repository.BookEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookEsService {
    public static final int PAGE_SIZE = 10;
    @Autowired
    private BookEsRepository bookEsRepository;

    public BookEs findById(String bookId) {
        return bookEsRepository.findById(bookId).orElse(null);
    }

    public Page<BookEs> getPagedBooks(int page, String field, String query) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        if (field.equals("title"))
            return bookEsRepository.findByTitleContaining("query", pageable);
        if (field.equals("author"))
            return bookEsRepository.findByAuthorContaining("query", pageable);
        if (field.equals("company"))
            return bookEsRepository.findByCompanyContaining("query", pageable);
        return bookEsRepository.findBySummaryContaining("query", pageable);
    }

    public void insertBookEs(BookEs bookEs) {
        bookEsRepository.save(bookEs);
    }

}
