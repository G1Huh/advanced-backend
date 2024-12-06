package com.lion.demo.controller;
/*
    -- 통합테스트 --
 */

import com.lion.demo.entity.Book;
import com.lion.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @BeforeEach
    void setup() {

    }

    @Test
    void testList() throws Exception {
        // Given
        Book book1 = Book.builder().bid(999L).title("title1").price(23000).build();
        Book book2 = Book.builder().bid(1000L).title("title2").price(24000).build();

        List<Book> bookList = Arrays.asList(book1, book2);
        when(bookService.getBookList(1, "title", "")).thenReturn(bookList);

        // When & Then
        mockMvc.perform(get("/book/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookList"))
                .andExpect(model().attributeExists("query"))
                .andExpect(view().name("book/list"));


    }
}
