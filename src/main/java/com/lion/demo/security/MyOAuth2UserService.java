package com.lion.demo.security;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String uid, email, uname, profileUrl;
        String hashedPwd = bCryptPasswordEncoder.encode("Social Login");
        User user = null;

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("%%% getAttributes %%%" + oAuth2User.getAttributes());

        // 소셜 로그인
        String provider = userRequest.getClientRegistration().getRegistrationId();
        switch (provider) {
            case "github":
                int id = oAuth2User.getAttribute("id");
                uid = provider + "_" + id;
                user = userService.findByUid(uid);
                if (user == null) {
                    // DB에 정보 미존재 -> 회원 등록
                    uname = oAuth2User.getAttribute("name");
                    email = oAuth2User.getAttribute("email");
                    profileUrl = oAuth2User.getAttribute("avatar_url");

                    user = User.builder()
                            .uid(uid)
                            .uname(uname)
                            .pwd(hashedPwd)
                            .uname(uname)
                            .email(email)
                            .regDate(LocalDate.now())
                            .role("ROLE_USER")
                            .profileUrl(profileUrl)
                            .provider(provider)
                            .build();
                    userService.registerUser(user);
                    log.info("깃허브 계정을 통해 회원가입이 되었습니다. " + user.getUname());
                }
                break;
            case "google":
                String sub = oAuth2User.getAttribute("sub");    // Google ID
                uid = provider + "_" + sub;
                user = userService.findByUid(uid);
                if (user == null) {
                    // DB에 정보 미존재 -> 회원 등록
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null)? "google_user" : uname;
                    email = oAuth2User.getAttribute("email");
                    profileUrl = oAuth2User.getAttribute("picture");

                    user = User.builder()
                            .uid(uid)
                            .uname(uname)
                            .pwd(hashedPwd)
                            .uname(uname)
                            .email(email)
                            .regDate(LocalDate.now())
                            .role("ROLE_USER")
                            .profileUrl(profileUrl)
                            .provider(provider)
                            .build();
                    userService.registerUser(user);
                    log.info("구글 계정을 통해 회원가입이 되었습니다. " + user.getUname());
                }
                break;

        }


        return new MyUserDetails(user, oAuth2User.getAttributes());
    }
}
