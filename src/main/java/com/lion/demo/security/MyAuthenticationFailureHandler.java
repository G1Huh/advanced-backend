package com.lion.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = "비밀번호가 틀렸습니다.";
        String url = "/user/login";

        // 예외 메시지 확인
        if (exception.getMessage().contains("사용자를 찾을 수 없습니다.")) {
            msg = exception.getMessage();
            url = "/user/register";
        }


        /*

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        // 특정 페이지로 이동
        request.getRequestDispatcher("/common/alertMsg.html").forward(request, response);


         */

        //실패 메시지 전달
        request.getSession().setAttribute("error", msg);

        // 리다이렉션
        response.sendRedirect("/user/login");

    }
}
