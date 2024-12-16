package com.lion.demo.controller;

import com.lion.demo.service.CsvFileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("restaurant")
public class RestaurantController {
    @Autowired
    private CsvFileReaderService csvFileReaderService;
    @GetMapping("/list")
    public String list(){
        return "";

    }



    @GetMapping("/init")
    @ResponseBody
    public String init(){
        csvFileReaderService.csvFileToElasticSearch();
        return "<h1>Done</h1>";
    }
}
