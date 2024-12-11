package com.lion.demo.config;

import com.lion.demo.chatting.ChattingHandShakeInterceptor;
import com.lion.demo.websocket.EchoWebSocketHandler;
import com.lion.demo.chatting.ChattingWebSocketHandler;
import com.lion.demo.websocket.UserHandShakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private EchoWebSocketHandler echoWebSocketHandler;
    @Autowired
    private ChattingWebSocketHandler personalWebSocketHandler;
    @Autowired
    private ChattingWebSocketHandler chattingWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler, "/echo")
                .setAllowedOrigins("*");    // 모든 도메인에서 접근 가능

        registry.addHandler(personalWebSocketHandler, "/personal")
                .addInterceptors(new UserHandShakeInterceptor())    // 1:1 Messaging
                .setAllowedOrigins("*");

        registry.addHandler(chattingWebSocketHandler, "/chat")
                .addInterceptors(new ChattingHandShakeInterceptor())
                .setAllowedOrigins("*");

    }
}
