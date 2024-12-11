package com.lion.demo.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {

    @GetMapping("/echo")
    public String echo(){
        return "websocket/echo";
    }

    @GetMapping("/echo2")
    public String echo2(){
        return "websocket/echo2";
    }
    @GetMapping("/personal")
    public String personal(){
        return "websocket/personal";
    }
}
