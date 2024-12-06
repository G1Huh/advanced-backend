package com.lion.demo.controller;

import com.lion.demo.entity.Book;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CsvFileReaderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private CsvFileReaderService csvFileReaderService;
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public String list(@RequestParam(name = "p", defaultValue = "1") int page,
                       @RequestParam(name = "f", defaultValue = "title") String field,
                       @RequestParam(name = "q", defaultValue = "") String query,
                       HttpSession session, Model model) {
//        List<Book> bookList = bookService.getBooksByPage(page);
        Page<Book> pagedResult = bookService.getPagedBooks(page, field, query);
        int totalPages = pagedResult.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / BookService.PAGE_SIZE - 1) * BookService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + BookService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("menu", "book");
        session.setAttribute("currentBookPage", page);
        model.addAttribute("bookList", pagedResult.getContent());
        model.addAttribute("field", field);
        model.addAttribute("query", query);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "book/list";
    }

    @GetMapping("/detail/{bid}")
    public String detail(@PathVariable long bid,
                         @RequestParam(name = "q", defaultValue = "") String query,
                         Model model) {
        Book book = bookService.findByBid(bid);
        // 검색어가 있을 때 요약 -> 하이라이트
        if (!query.equals("")) {
            String highlightedSummary = book.getSummary().replaceAll(query, "<span style='background-color: skyblue;'>"
                    + query + "</span>");
            book.setSummary(highlightedSummary);
        }
        model.addAttribute("book", book);

        return "book/detail";
    }

    @GetMapping("/insert")
    public String insertForm() {
        return "book/insert";
    }

    @PostMapping("/insert")
    public String insertProc(Book book) {
        bookService.insertBook(book);
        return "redirect:/book/list";
    }

    // csv file -> h2
    @GetMapping("/yes24")
    public String yes24() {
        csvFileReaderService.csvFileToH2();
        return "redirect:/user/list";
    }

}
