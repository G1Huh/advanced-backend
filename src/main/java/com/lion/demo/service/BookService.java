package com.lion.demo.service;

import com.lion.demo.entity.Book;

import java.util.List;

public interface BookService {
    Book findByBid(long bid);
    List<Book> getBooks();
    void insertBook(Book book);
    void updateBook(Book book);
    void deleteBook(long bid);
}
