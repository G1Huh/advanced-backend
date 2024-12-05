package com.lion.demo.security;

import com.lion.demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 스프링 시큐리티가 로그인 포스트 요청을 낚아채서 로그인을 진행시킴

// 로컬 로그인 - UserDetails 구현
// 소셜 로그인 -   구현
public class MyUserDetails implements UserDetails, OAuth2User {
    // 로컬 로그인
    private User user;
    // 소셜 로그인
    private Map<String, Object> attributes;

    public MyUserDetails(){

    }
    public MyUserDetails(User user) {
        this.user = user;
    }
    public MyUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                System.out.println("getAuthority(): " + user.getRole());
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    @Override
    public String getName() {
        return null;
    }
}
