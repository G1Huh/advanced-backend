package com.lion.demo.controller;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String registerForms() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email) {
        // 신규 가입
        if (userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4) {
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
    public String list(HttpSession session, Model model) {
        List<User> userList = userService.getUsers();

        session.setAttribute("menu", "user");
        model.addAttribute("userList", userList);

        return "user/list";
    }

    // QueryParameter --> /delete?uid=james 와 같은 형식은 구식 스타일
    // PathVariable --> /delete/{uid} 와 같은 형식을 요즘은 더 많이 사용
    @GetMapping("/delete/{uid}")
    public String delete(@PathVariable String uid) {
        userService.deleteUser(uid);
        return "redirect:/user/list";
    }

    @GetMapping("/update/{uid}")
    public String updateForm(@PathVariable String uid, Model model) {
        User user = userService.findByUid(uid);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/update")
    public String updateProc(String uid, String pwd, String pwd2, String uname, String email, String role) {
        User user = userService.findByUid(uid);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            user.setPwd(hashedPwd);
        }
        user.setUname(uname);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);

        return "redirect:/user/list";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginProc(String uid, String pwd, HttpSession session, Model model) {
        String msg, url;    // 출력할 alert 메시지 및 redirect할 url
        int result = userService.login(uid, pwd);

        if (result == UserService.CORRECT_LOGIN) {
            // 로그인 성공 시
            User user = userService.findByUid(uid);
            session.setAttribute("sessUid", uid);
            session.setAttribute("sessUname", user.getUname());
            msg = user.getUname() + "님 환영합니다";
            url = "/book/list";
        } else if (result == UserService.WRONG_PASSWORD) {
            // 잘못된 비밀번호
            msg = "패스워드가 틀렸습니다.";
            url = "/user/login";
        } else {
            // 회원 정보 미존재
            msg = "입력한 아이디가 존재하지 않습니다.";
            url = "/user/register";
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        return "common/alertMsg";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model) {
        // Sprig Security 현재 세션의 사용자 아이디
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        User user = userService.findByUid(uid);

        session.setAttribute("sessUid", uid);
        session.setAttribute("sessUname", user.getUname());

        String msg = user.getUname() + "님 환영합니다";
        String url = "/chatting/home";

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        return "common/alertMsg";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 로그아웃 -> 세션 초기화
        session.invalidate();
        return "redirect:/user/login";
    }
}
