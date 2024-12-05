package com.lion.demo.repository;

import com.lion.demo.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 테스트마다 트랜잭션이 생성되고, 테스트가 끝나면 자동으로 롤백

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    
    @Test
    void testSaveAndFindBook(){
        // Given
        Book book = new Book(0, "title", "author", "compay", 20000, "Image Url", "summary");
        // When
        bookRepository.save(book);
        // Then
        List<Book> bookList = bookRepository.findAll();
        int size = bookList.size();
        System.out.println("size = " + size);

        Assertions.assertThat(bookList).hasSize(1);
        Assertions.assertThat(bookList.get(0).getTitle()).isEqualTo("title");
        Assertions.assertThat(bookList.get(0).getPrice()).isEqualTo(20000);
        
    }

    @Test
    void testSaveAndFindBookByTitle(){
        // Given
        Book book = new Book(0, "title", "author", "compay", 20000, "Image Url", "summary");
        Book book2 = new Book(0, "title", "author", "compay", 20000, "Image Url", "summary");

        // When
        bookRepository.save(book);
        bookRepository.save(book2);
        // Then
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = bookRepository.findByTitleContaining("title", pageable).getContent();
        int size = bookList.size();
        System.out.println("size = " + size);

        Assertions.assertThat(bookList).hasSize(2);
        Assertions.assertThat(bookList.get(0).getTitle()).isEqualTo("title");
        Assertions.assertThat(bookList.get(0).getPrice()).isEqualTo(20000);


    }
}
