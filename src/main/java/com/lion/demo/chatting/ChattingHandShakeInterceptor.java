package com.lion.demo.chatting;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ChattingHandShakeInterceptor implements HandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest){
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
            String userId = httpRequest.getServletRequest().getParameter("userId");
            String status = httpRequest.getServletRequest().getParameter("status");

            if(userId != null){
                attributes.put("userId", userId);
                attributes.put("status", status);
                System.out.println("=====================beforeHandShake : userId : " + userId + ", status : " + status);

            }else{
                System.out.println("=====================beforeHandshake : userId is null");
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 필요시 추가
    }
}
