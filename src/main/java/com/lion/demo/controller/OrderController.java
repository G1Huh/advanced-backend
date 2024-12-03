package com.lion.demo.controller;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.entity.OrderItem;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.OrderService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/createOrder")
    public String createOrder(HttpSession session){
        String uid = (String) session.getAttribute("sessUid");
        // 해당 사용자의 장바구니 리스트
        List<Cart> cartList = cartService.getCartItemsByUser(uid);
        // 사용자Id와 장바구니 리스트로 주문 생성
        Order order = orderService.createOrder(uid, cartList);

        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model){
        String uid = (String) session.getAttribute("sessUid");
        // 주문 리스트
        List<Order> orderList = orderService.getOrdersByUser(uid);
        // 각 주문별 대표 책 제목 및 상세 주문 건수 출력
        List<String> orderTitleList = new ArrayList<>();
        for(Order order: orderList){
            List<OrderItem> orderItems = order.getOrderItems();
            String title = orderItems.get(0).getBook().getTitle();  // 첫 번째 상세주문의 책 제목
            int size = orderItems.size();   // 상세 주문 갯수
            if(size > 1)    // 책 종류(상세주문)가 1권 이상일 때 대표값 제외한 상세주문 갯수
                title += " 외 " + (size -1) + "건";
            orderTitleList.add(title);
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);

        return "order/list";
    }

    // 관리자 모드 - 모든 주문내역 보기
    @GetMapping("/listAll")
    public String listAll(Model model){
        // 기간 설정 : 024년 12월 주문 리스트
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 01, 00, 00 );
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999);
        
        int totalRevenue = 0;   // 총 판매금액
        int totalBooks = 0;     // 총 판매된 책 갯수
        
        // 해당 기간 내 주문 리스트
        List<Order> orderList = orderService.getOrdersByDateRange(startTime, endTime);
        // 각 주문별 대표 책 제목 및 상세 주문 건수
        List<String> orderTitleList = new ArrayList<>();
        for(Order order: orderList){
            totalRevenue += order.getTotalAmount(); // 관리자 - 총 판매액
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems)   // 관리자 - 총 판매부수
                totalBooks += orderItem.getQuantity();            
            String title = orderItems.get(0).getBook().getTitle();  // 첫 번째 상세주문의 책 제목
            int size = orderItems.size();   // 상세 주문 갯수
            if(size > 1)    // 책 종류(상세주문)가 1권 이상일 때 대표값 제외한 상세주문 갯수
                title += " 외 " + (size -1) + "건";
            orderTitleList.add(title);
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalBooks", totalBooks);

        return "order/listAll";
    }
}
