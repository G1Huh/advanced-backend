package com.lion.demo.controller;

import com.lion.demo.entity.BookEsDto;
import com.lion.demo.entity.Restaurant;
import com.lion.demo.entity.RestaurantDto;
import com.lion.demo.service.BookEsService;
import com.lion.demo.service.CsvFileReaderService;
import com.lion.demo.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("restaurant")
public class RestaurantController {
    @Autowired
    private CsvFileReaderService csvFileReaderService;
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/list")
    public String list(@RequestParam(name="p", defaultValue = "1") int page,
                       @RequestParam(name="f", defaultValue = "title") String field,
                       @RequestParam(name="q", defaultValue = "") String query,
                       HttpSession session, Model model) {

        Page<RestaurantDto> pagedResult = restaurantService.getPagedRestaurants(page, field, query);
        int totalPages = pagedResult.getTotalPages();
        int startPage = (int) Math.ceil((page - 0.5) / BookEsService.PAGE_SIZE - 1) * BookEsService.PAGE_SIZE + 1;
        int endPage = Math.min(startPage + BookEsService.PAGE_SIZE - 1, totalPages);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++)
            pageList.add(i);

        session.setAttribute("menu", "elastic");
        session.setAttribute("currentRestaurantPage", page);
        model.addAttribute("restaurantDtoList", pagedResult.getContent());
        model.addAttribute("field", field);
        model.addAttribute("query", query);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageList", pageList);
        return "restaurant/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model){
        Restaurant restaurant = restaurantService.findById(id);
        Map<String, Object> infoMap = new HashMap<>();
        for(String key: restaurant.getInfo().keySet()){
            if(key.equals("전화번호"))
                continue;
            infoMap.put(key, restaurant.getInfo().get(key));
        }

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("infoCount", restaurant.getInfo().size());

        return "restaurant/detail";
    }


    @GetMapping("/init")
    @ResponseBody
    public String init(){
        csvFileReaderService.RestaurantSeoulToElasticSearch();
        return "<h1>Done</h1>";
    }
}
