package com.lion.demo.controller;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForms(){
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email){
        // 신규 가입
        if(userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4){
            // 입력된 패스워드 -> 암호화하여 DB 저장
            // 암호화된 DB값은 복호화 불가 (로그인시 입력된 pw값을 암호화하여 암호화된 DB값과 비교)
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            User user = User.builder()
                            .uid(uid)
                            .pwd(hashedPwd)
                            .uname(uname)
                            .email(email)
                            .regDate(LocalDate.now())
                            .role("ROLE_USER")
                            .build();
            // 아래와 같이도 작성 가능하지만 모든 타입이 String 이기 때문에 파라미터 순서가 바뀌어도 에러 발생하지 않으나 로직상 문제있음.
            // User user = new User(uid, hashPwd, uname, email, LocalDate.now(), "ROLE_USER");
            // 따라서 builder를 활용한 코드가 실수 없이 더 정확함.

            userService.registerUser(user);
        }
        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "user/list";

    }
}
