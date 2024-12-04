package com.lion.demo.service;

import com.lion.demo.entity.Book;

import java.util.List;

public interface BookService {
    public static final int PAGE_SIZE = 10;
    Book findByBid(long bid);
//    List<Book> getBooks();
    List<Book> getBooksByPage(int page);
    List<Book> getBookList(int page, String field, String query);
    void insertBook(Book book);
    void updateBook(Book book);
    void deleteBook(long bid);
}
