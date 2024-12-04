package com.lion.demo.aspect;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class PermissionAspect {
    @Autowired
    private UserService userService;

    @Before("@annotation(checkPermission)")
    public void checkPermission(JoinPoint jointPoint, CheckPermission checkPermission) throws IllegalAccessException {
        // 현재 요청의 HttpSession 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null){
            throw new IllegalAccessException("현재 요청에서 HttpSession을 가져올 수 없습니다.");
        }
        HttpSession session = attributes.getRequest().getSession();

        String requiredPermission = checkPermission.value();
        String uid = (String) session.getAttribute("sessUid");
        User currentUser = userService.findByUid(uid);

        if(!currentUser.getRole().equals(requiredPermission)){
            throw new SecurityException("권한 부족 : " + requiredPermission);
        }
        System.out.println("권한 검증 통과 : " + jointPoint.getSignature());
    }

}
