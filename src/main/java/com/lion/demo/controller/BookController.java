package com.lion.demo.controller;

import com.lion.demo.service.BookService;
import com.lion.demo.service.CsvFileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private CsvFileReaderService csvFileReaderService;
    @Autowired
    private BookService bookService;

    @GetMapping("/yes24")
    public String yes24(){
        csvFileReaderService.csvFileToH2();
        return "redirect:/user/list";
    }
}
