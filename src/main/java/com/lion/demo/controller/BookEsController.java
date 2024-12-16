package com.lion.demo.controller;

import com.lion.demo.entity.BookEs;
import com.lion.demo.entity.BookEsDto;
import com.lion.demo.service.BookEsService;
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
@RequestMapping("/bookEs")
public class BookEsController {
    @Autowired private BookEsService bookEsService;
    @Autowired private CsvFileReaderService csvFileReaderService;

    @GetMapping("/list")
    public String list(@RequestParam(name="p", defaultValue = "1") int page,
                       @RequestParam(name="f", defaultValue = "title") String field,
                       @RequestParam(name="q", defaultValue = "") String query,
                       HttpSession session, Model model) {

        Page<BookEsDto> pagedResult = bookEsService.getPagedBooks(page, field, query);
        int totalPages = pagedResult.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / BookEsService.PAGE_SIZE - 1) * BookEsService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + BookEsService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("menu", "bookEs");
        session.setAttribute("currentBookEsPage", page);
        model.addAttribute("bookEsDtoList", pagedResult.getContent());
        model.addAttribute("field", field);
        model.addAttribute("query", query);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "bookEs/list";
    }

    @GetMapping("/detail/{bookId}")
    public String detail(@PathVariable String bookId,
                         @RequestParam(name="q", defaultValue = "**") String query,
                         Model model) {
        BookEs bookEs = bookEsService.findById(bookId);
        if (!query.equals("")) {
            String highlightedSummary = bookEs.getSummary()
                    .replaceAll(query, "<span style='background-color: skyblue;'>" + query + "</span>");
            bookEs.setSummary(highlightedSummary);
        }
        model.addAttribute("bookEs", bookEs);
        return "bookEs/detail";
    }




    @GetMapping("/yes24")
    @ResponseBody
    public String yes24() {
        csvFileReaderService.csvFileToElasticSearch();
        return "<h1>일래스틱서치에 데이터를 저장했습니다.</h1>";
    }
}