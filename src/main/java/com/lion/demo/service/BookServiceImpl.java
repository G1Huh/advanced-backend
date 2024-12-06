package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book findByBid(long bid) {
        return bookRepository.findById(bid).orElse(null);
    }

//    @Override
//    public List<Book> getBooks() {
//        return bookRepository.findAll();
//    }

    @Override
    public List<Book> getBooksByPage(int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Book> bookPage = bookRepository.findAll(pageable);

        return bookPage.getContent();
    }

    @Override
    public List<Book> getBookList(int page, String field, String query) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Book> bookPage;
        if (field.equals("title"))
            bookPage = bookRepository.findByTitleContaining(query, pageable);
        else if (field.equals("author"))
            bookPage = bookRepository.findByAuthorContaining(query, pageable);
        else if (field.equals("company"))
            bookPage = bookRepository.findByCompanyContaining(query, pageable);
        else
            bookPage = bookRepository.findBySummaryContaining(query, pageable);
        return bookPage.getContent();
    }

    @Override
    public Page<Book> getPagedBooks(int page, String field, String query) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Book> bookPage = null;
        if (field.equals("title"))
            bookPage = bookRepository.findByTitleContaining(query, pageable);
        else if (field.equals("author"))
            bookPage = bookRepository.findByAuthorContaining(query, pageable);
        else if (field.equals("company"))
            bookPage = bookRepository.findByCompanyContaining(query, pageable);
        else
            bookPage = bookRepository.findBySummaryContaining(query, pageable);
        return bookPage;
    }

    @Override
    public void insertBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(long bid) {
        bookRepository.deleteById(bid);
    }
}
